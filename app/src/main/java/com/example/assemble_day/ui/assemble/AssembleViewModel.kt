package com.example.assemble_day.ui.assemble

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.ui.common.CalendarUtil
import java.time.LocalDate

class AssembleViewModel : ViewModel() {

    private val _calendarDataMap = mutableMapOf<LocalDate, List<CalendarDay>>()
    val calendarDataMap: Map<LocalDate, List<CalendarDay>> = _calendarDataMap

    init {
        createCalendarData()
    }

    private fun createCalendarData() {
        val calendarData = Array<LocalDate>(12) { LocalDate.now().plusMonths(it.toLong()) }
        calendarData.forEach {
            _calendarDataMap[it] = CalendarUtil.getDayList(it)
        }
    }

}