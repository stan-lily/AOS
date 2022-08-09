package com.example.assemble_day.ui.common

import com.example.assemble_day.common.Constants.CALENDAR_DAY_OF_MONTH_RANGE_END
import com.example.assemble_day.common.Constants.CALENDAR_DAY_OF_MONTH_RANGE_START
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_FOUR_HUNDRED_YEAR
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_FOUR_YEAR
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_HUNDRED_YEAR
import com.example.assemble_day.common.Constants.SATURDAY_DAY_OF_WEEK
import com.example.assemble_day.common.Constants.SUNDAY_DAY_OF_WEEK
import com.example.assemble_day.domain.model.CalendarDay
import java.time.*

object CalendarUtil {

    fun getDayList(date: LocalDate): List<CalendarDay> {
        val year = date.year
        val month = date.month
        val firstDay = date.withDayOfMonth(1)
        val isLeapYear =
            (year % LEAP_YEAR_STANDARD_FOUR_YEAR == 0 && year % LEAP_YEAR_STANDARD_HUNDRED_YEAR != 0) || year % LEAP_YEAR_STANDARD_FOUR_HUNDRED_YEAR == 0
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
        for (i in CALENDAR_DAY_OF_MONTH_RANGE_START..CALENDAR_DAY_OF_MONTH_RANGE_END) {
            if (i <= firstDayOfWeek || i > lastDay + firstDayOfWeek) {
                dayList.add(CalendarDay(year = year, month = month))
            } else {
                val day = i - firstDayOfWeek
                val isSaturday =
                    LocalDateTime.of(year, month, day, 0, 0).dayOfWeek.value == SATURDAY_DAY_OF_WEEK
                val isSunday =
                    LocalDateTime.of(year, month, day, 0, 0).dayOfWeek.value == SUNDAY_DAY_OF_WEEK
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