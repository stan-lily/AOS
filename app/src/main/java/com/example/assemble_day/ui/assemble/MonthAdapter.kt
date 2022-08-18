package com.example.assemble_day.ui.assemble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemCalendarMonthBinding
import com.example.assemble_day.domain.model.CalendarDay

class MonthAdapter(private val itemClick: (calendarDay: CalendarDay) -> Unit) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private val monthList = mutableListOf<String>()
    private val calendarData = mutableMapOf<String, DayAdapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding =
            ItemCalendarMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(monthList[position])
    }

    override fun getItemCount(): Int {
        return monthList.size
    }

    inner class MonthViewHolder(private val binding: ItemCalendarMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(month: String) {
            binding.month = month

            calendarData[month]?.let {
                binding.rvCalendarMonth.adapter = it
            }
        }

    }

    fun submitCalendarData(calendarDataMap: Map<String, List<CalendarDay>>) {
        monthList.removeAll(monthList)
        calendarDataMap.forEach {
            val dayAdapter = DayAdapter(itemClick)
            calendarData[it.key] = dayAdapter.apply {
                submitList(it.value)
            }
        }
        monthList.addAll(calendarDataMap.keys)
    }

    fun submitNewCalendarDateList(month: String, list: List<CalendarDay>) {
        calendarData[month]?.submitList(list.toList())
    }
}