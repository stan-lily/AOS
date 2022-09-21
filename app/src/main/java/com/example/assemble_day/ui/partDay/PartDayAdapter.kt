package com.example.assemble_day.ui.partDay

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.draganddrop.DropHelper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemPartDayBinding
import com.example.assemble_day.ui.common.eventListener.PartDayEventListener
import com.example.assemble_day.domain.model.PartDay
import com.example.assemble_day.domain.model.PartDayTarget

class PartDayAdapter(private val eventListener: PartDayEventListener) :
    ListAdapter<PartDay, PartDayAdapter.PartDayViewHolder>(
        PartDayDiffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartDayViewHolder {
        val binding =
            ItemPartDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartDayViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PartDayViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class PartDayViewHolder(private val binding: ItemPartDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(partDay: PartDay) {
            binding.partDay = partDay
            setOnDropListener()
            setOnClickListener(partDay)
        }

        private fun setOnClickListener(selectedPartDay: PartDay) {
            itemView.setOnClickListener {
                eventListener.selectPartDay(selectedPartDay)
            }
        }

        private fun setOnDropListener() {
            DropHelper.configureView(
                binding.root.context as Activity,
                itemView,
                arrayOf(ClipDescription.MIMETYPE_TEXT_INTENT),
            ) { _, payload ->
                val item = payload.clip.getItemAt(0)
                val (_, remaining) = payload.partition { it == item }

                when {
                    payload.clip.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) ->
                        handleTargetDrop(item)
                }
                remaining
            }
        }

        private fun handleTargetDrop(item: ClipData.Item) {
            val droppedTarget = item.intent.getSerializableExtra("target") as PartDayTarget
            val droppedTargetPosition = item.intent.getIntExtra("position", -1)
            eventListener.dropTargetToOtherPartDay(
                droppedTarget,
                droppedTargetPosition,
                adapterPosition
            )
        }

    }

    companion object PartDayDiffUtil : DiffUtil.ItemCallback<PartDay>() {

        override fun areItemsTheSame(oldItem: PartDay, newItem: PartDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: PartDay,
            newItem: PartDay
        ): Boolean {
            return oldItem == newItem
        }
    }

}