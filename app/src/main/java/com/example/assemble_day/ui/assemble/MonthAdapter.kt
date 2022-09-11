package com.example.assemble_day.ui.assemble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.common.Constants.CALENDAR_DAY_SIZE
import com.example.assemble_day.databinding.ItemCalendarMonthBinding
import com.example.assemble_day.domain.model.CalendarDay
import java.time.LocalDate

class MonthAdapter(private val itemClick: (calendarDay: CalendarDay) -> Unit) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private val monthList = mutableListOf<LocalDate>()
    private val calendarData = mutableMapOf<LocalDate, DayAdapter>()

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
            calendarData[month]?.let {
                binding.rvCalendarMonth.adapter = it
            }
        }

    }

    fun submitCalendarData(calendarDataMap: Map<LocalDate, List<CalendarDay>>) {
        monthList.clear()
        calendarDataMap.forEach {
            val dayAdapter = DayAdapter(itemClick)
            calendarData[it.key] = dayAdapter.apply {
                submitList(it.value)
            }
        }
        monthList.addAll(calendarDataMap.keys)
        notifyItemRangeChanged(0, CALENDAR_DAY_SIZE - 1)
    }

   /* fun submitNewCalendarDateList(month: LocalDate?, list: List<CalendarDay>) {
        month?.let {
            calendarData[it]?.submitList(list.toList())
        }
    }*/
}