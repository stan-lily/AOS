package com.example.assemble_day.ui.partDay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemPartDayBinding
import com.example.assemble_day.domain.model.PartDay
import java.time.format.DateTimeFormatter

class PartDayAdapter :
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

    class PartDayViewHolder(private val binding: ItemPartDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(assembleDayUnit: PartDay) {
            val formatter = DateTimeFormatter.ofPattern("dd")
            binding.date = assembleDayUnit.date.format(formatter)
        }

    }

    companion object PartDayDiffUtil : DiffUtil.ItemCallback<PartDay>() {

        override fun areItemsTheSame(oldItem: PartDay, newItem: PartDay): Boolean {
            return oldItem.assembleDay == newItem.assembleDay
        }

        override fun areContentsTheSame(
            oldItem: PartDay,
            newItem: PartDay
        ): Boolean {
            return oldItem == newItem
        }
    }

}