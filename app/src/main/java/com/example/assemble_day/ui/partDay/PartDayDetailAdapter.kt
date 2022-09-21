package com.example.assemble_day.ui.partDay

import android.content.ClipData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.DragStartHelper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemPartDayTargetBinding
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.model.TargetItemSelection

class PartDayDetailAdapter(
    private val itemClick: (itemSelection: TargetItemSelection, itemPosition: Int) -> Unit
) :
    ListAdapter<PartDayTarget, PartDayDetailAdapter.PartDayDetailViewHolder>(
        PartDayDetailDiffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartDayDetailViewHolder {
        val binding =
            ItemPartDayTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartDayDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PartDayDetailViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class PartDayDetailViewHolder(private val binding: ItemPartDayTargetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(target: PartDayTarget) {
            binding.target = target
            binding.isSelected = target.isSelected
            setTargetLabelBtnOnClickListener()
            setTargetOnClickListener()
            setTargetUpdateBtnOnClickListener()
            setTargetDeleteBtnOnClickListener()
            setOnDragListener(target)

        }

        private fun setTargetLabelBtnOnClickListener() {
            binding.btnTargetLabel.setOnClickListener {
                itemClick.invoke(TargetItemSelection.LabelSection, adapterPosition)
            }
        }

        private fun setTargetOnClickListener() {
            binding.clTarget.setOnClickListener {
                itemClick.invoke(TargetItemSelection.TargetSelection, adapterPosition)
            }
        }

        private fun setTargetUpdateBtnOnClickListener() {
            binding.btnTargetUpdate.setOnClickListener {
                itemClick.invoke(
                    TargetItemSelection.TargetUpdateBtnSelection(binding.etTarget.text.toString()),
                    adapterPosition
                )
            }
        }

        private fun setTargetDeleteBtnOnClickListener() {
            binding.btnTargetDelete.setOnClickListener {
                itemClick.invoke(TargetItemSelection.TargetDeleteBtnSelection, adapterPosition)
            }
        }

        private fun setOnDragListener(target: PartDayTarget) {
            DragStartHelper(itemView) { view, _ ->
                val targetIntent = Intent().apply {
                    putExtra("target", target)
                    putExtra("position", adapterPosition)
                }

                val dragClipData = ClipData.newIntent("target", targetIntent)
                val dragShadowBuilder = View.DragShadowBuilder(view)

                view.startDragAndDrop(
                    dragClipData,
                    dragShadowBuilder,
                    null,
                    View.DRAG_FLAG_GLOBAL
                )
            }.attach()
        }

    }

    companion object PartDayDetailDiffUtil : DiffUtil.ItemCallback<PartDayTarget>() {

        override fun areItemsTheSame(oldItem: PartDayTarget, newItem: PartDayTarget): Boolean {
            return oldItem.labelId == newItem.labelId && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: PartDayTarget,
            newItem: PartDayTarget
        ): Boolean {
            return oldItem == newItem
        }
    }

}