package com.example.assemble_day.ui.partDay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemPartDayDetailBinding
import com.example.assemble_day.domain.model.PartDayTarget

class PartDayDetailAdapter :
    ListAdapter<PartDayTarget, PartDayDetailAdapter.PartDayDetailViewHolder>(
        PartDayDetailDiffUtil
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartDayDetailViewHolder {
        val binding =
            ItemPartDayDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartDayDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PartDayDetailViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class PartDayDetailViewHolder(private val binding: ItemPartDayDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(target: PartDayTarget) {
            binding.target = target
            println("바인드됨")
        }

    }

    companion object PartDayDetailDiffUtil : DiffUtil.ItemCallback<PartDayTarget>() {

        override fun areItemsTheSame(oldItem: PartDayTarget, newItem: PartDayTarget): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: PartDayTarget,
            newItem: PartDayTarget
        ): Boolean {
            return oldItem == newItem
        }
    }

}