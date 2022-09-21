package com.example.assemble_day.ui.milestone

import android.view.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemMilestoneBinding
import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.domain.model.Milestone
import com.example.assemble_day.ui.common.eventListener.MilestoneEventListener

class MilestoneAdapter(
    private val itemEventListener: MilestoneEventListener
) : ListAdapter<Milestone, MilestoneAdapter.MilestoneViewHolder>(MilestoneDiffUtil) {

    private var isEditMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding = ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MilestoneViewHolder(private val binding: ItemMilestoneBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(milestone: Milestone) {
            binding.milestone = milestone
            binding.cbMilestoneSelector.isChecked = milestone.isChecked
            longClickMilestone()
            showCheckBox()
            setCheckBoxOnCheckListener(milestone)
        }

        private fun longClickMilestone() {
            binding.clMilestone.setOnLongClickListener {
                itemEventListener.switchToIssueEditMode()
                true
            }
        }

        private fun showCheckBox() {
            binding.cbMilestoneSelector.isVisible = isEditMode
            if (!isEditMode) binding.cbMilestoneSelector.isChecked = false
        }

        private fun setCheckBoxOnCheckListener(milestone: Milestone) {
            binding.cbMilestoneSelector.setOnClickListener {
                if (milestone.isChecked) itemEventListener.uncheckMilestone(adapterPosition)
                else itemEventListener.checkMilestone(adapterPosition)
            }

//            binding.cbMilestoneSelector.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) itemEventListener.checkMilestone(milestone)
//                else itemEventListener.uncheckMilestone(milestone)
//            }
        }

    }

    fun showIssueEditMode() {
        isEditMode = true
        notifyItemRangeChanged(0, this.currentList.size)
    }

    fun closeIssueEditMode() {
        isEditMode = false
        notifyItemRangeChanged(0, this.currentList.size)
    }

    companion object MilestoneDiffUtil : DiffUtil.ItemCallback<Milestone>() {

        override fun areItemsTheSame(oldItem: Milestone, newItem: Milestone): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Milestone, newItem: Milestone): Boolean {
            return oldItem == newItem
        }
    }

}
