package com.example.assemble_day.ui.assemble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.databinding.ItemCalendarMonthBinding
import com.example.assemble_day.domain.model.CalendarDay
import java.time.LocalDate

class MonthAdapter(private val itemClick: (calendarDay: CalendarDay) -> Unit) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private val monthList = mutableListOf<LocalDate>()
    private val calendarData = mutableMapOf<LocalDate, List<CalendarDay>>()
    private var startDay: CalendarDay? = null
    private var endDay: CalendarDay? = null

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

        fun bind(month: LocalDate) {
            binding.month = month

            val dayAdapter = DayAdapter(itemClick)
            binding.rvCalendarMonth.adapter = dayAdapter
            calendarData[month]?.let {
                dayAdapter.submitList(it)
            }
        }

    }

    fun submitCalendarData(calendarDataMap: Map<LocalDate, List<CalendarDay>>) {
        monthList.removeAll(monthList)
        calendarData.putAll(calendarDataMap)
        monthList.addAll(calendarDataMap.keys)
    }

    fun submitList(calendarDataMap: Map<LocalDate, List<CalendarDay>>) {

    }


}