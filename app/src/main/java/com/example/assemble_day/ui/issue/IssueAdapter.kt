package com.example.assemble_day.ui.issue

import android.view.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemIssueBinding
import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.ui.common.eventListener.IssueEventListener

class IssueAdapter(private val itemEventListener: IssueEventListener) : ListAdapter<Issue, IssueAdapter.IssueViewHolder>(IssueDiffUtil) {

    private var isEditMode = false
    private var rootWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val binding = ItemIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IssueViewHolder(private val binding: ItemIssueBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(issue: Issue) {
            binding.issue = issue
            showCheckBox()
            longClickIssue()
            setCheckBoxOnCheckListener(issue)
            setCloseTextOnClickListener()
        }

        private fun longClickIssue() {
            binding.clIssue.setOnLongClickListener {
                itemEventListener.switchToIssueEditMode()
                true
            }
        }

        private fun showCheckBox() {
            binding.cbIssueSelector.isVisible = isEditMode
            if (!isEditMode) binding.cbIssueSelector.isChecked = false
        }

        private fun setCheckBoxOnCheckListener(issue: Issue) {
            binding.cbIssueSelector.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) itemEventListener.checkIssue(issue)
                else itemEventListener.uncheckIssue(issue)

            }
        }

        fun getBinding() = binding

        private fun setCloseTextOnClickListener() {
            binding.tvIssueClose.setOnClickListener {
                itemEventListener.closeIssue(adapterPosition)
            }
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

    companion object IssueDiffUtil : DiffUtil.ItemCallback<Issue>() {

        override fun areItemsTheSame(oldItem: Issue, newItem: Issue): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Issue, newItem: Issue): Boolean {
            return oldItem == newItem
        }
    }

}
