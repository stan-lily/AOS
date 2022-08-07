package com.example.assemble_day.ui.common

import com.example.assemble_day.domain.model.CalendarDay
import java.time.*

object CalendarUtil {

    fun getDayList(date: LocalDate): List<CalendarDay> {
        val year = date.year
        val month = date.month
        val firstDay = date.withDayOfMonth(1)
        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
        val lastDay = month.length(isLeapYear)
        val firstDayOfWeek = firstDay.dayOfWeek.value
        val today = LocalDateTime.now().dayOfMonth
        val thisMonth = LocalDateTime.now().monthValue

        return createDayList(
                year = year,
                month = date.monthValue,
                firstDayOfWeek = firstDayOfWeek,
                lastDay = lastDay,
                today = today,
                thisMonth = thisMonth
            )
    }

    private fun createDayList(
        year: Int,
        month: Int,
        firstDayOfWeek: Int,
        lastDay: Int,
        today: Int,
        thisMonth: Int
    ): List<CalendarDay> {
        val dayList = mutableListOf<CalendarDay>()
        for (i in 2..43) {
            if (i <= firstDayOfWeek || i > lastDay + firstDayOfWeek) {
                dayList.add(CalendarDay(year = year, month = month))
            } else {
                val day = i - firstDayOfWeek
                val isSaturday = LocalDateTime.of(year, month, day, 0, 0).dayOfWeek.value == 6
                val isSunday = LocalDateTime.of(year, month, day, 0, 0).dayOfWeek.value == 7
                if (thisMonth == month) {
                    dayList.add(
                        CalendarDay(
                            year = year,
                            month = month,
                            day = day.toString(),
                            isSelectable = day >= today,
                            isSaturday = isSaturday,
                            isSunday = isSunday
                        )
                    )
                } else {
                    dayList.add(
                        CalendarDay(
                            year = year,
                            month = month,
                            day = day.toString(),
                            isSaturday = isSaturday,
                            isSunday = isSunday
                        )
                    )
                }
            }
        }
        return dayList
    }


}