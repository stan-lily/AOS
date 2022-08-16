package com.example.assemble_day.ui.assemble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemCalendarDayBinding
import com.example.assemble_day.domain.model.CalendarDay

class DayAdapter(private val itemClick: (calendarDay: CalendarDay) -> Unit) :
    ListAdapter<CalendarDay, DayAdapter.DayViewHolder>(DayDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding =
            ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DayViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: CalendarDay) {
            binding.calendarDay = day
            day.date?.let { date ->
                binding.day = date.dayOfMonth.toString()
            }
            setOnItemClickEventListener(day)
        }

        private fun setOnItemClickEventListener(day: CalendarDay) {
            binding.tvCalendarDay.setOnClickListener {
                if (day.isSelectable) itemClick.invoke(day)
            }
        }

    }

    companion object DayDiffUtil : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }
    }
}