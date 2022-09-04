package com.example.assemble_day.ui.labelFilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemLabelBinding
import com.example.assemble_day.domain.model.Label

class LabelFilterAdapter(private val itemClick: (position:Int) -> Unit) : ListAdapter<Label, LabelFilterAdapter.LabelViewHolder>(LabelDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val binding = ItemLabelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LabelViewHolder(private val binding: ItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(label: Label) {
            binding.label = label
            setOnItemClickListener(adapterPosition)
        }

        private fun setOnItemClickListener(position: Int) {
            binding.clItemLabel.setOnClickListener {
                itemClick.invoke(position)
            }
        }

    }

    companion object LabelDiffUtil : DiffUtil.ItemCallback<Label>() {
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }

    }
}