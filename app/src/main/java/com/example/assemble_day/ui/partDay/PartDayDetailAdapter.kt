package com.example.assemble_day.ui.partDay

import android.view.LayoutInflater
import android.view.ViewGroup
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
        }

        private fun setTargetLabelBtnOnClickListener() {
            binding.btnTargetLabel.setOnClickListener {
                itemClick.invoke(TargetItemSelection.labelSection, adapterPosition)
            }
        }

        private fun setTargetOnClickListener() {
            binding.clTarget.setOnClickListener {
                itemClick.invoke(TargetItemSelection.targetSelection, adapterPosition)
            }
        }

        private fun setTargetUpdateBtnOnClickListener() {
            binding.btnTargetUpdate.setOnClickListener {
                itemClick.invoke(
                    TargetItemSelection.targetUpdateBtnSelection(binding.etTarget.text.toString()),
                    adapterPosition
                )
            }
        }

        private fun setTargetDeleteBtnOnClickListener() {
            binding.btnTargetDelete.setOnClickListener {
                itemClick.invoke(TargetItemSelection.targetDeletBtnSelection, adapterPosition)
            }
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