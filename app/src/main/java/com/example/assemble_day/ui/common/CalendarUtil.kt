package com.example.assemble_day.ui.common

import com.example.assemble_day.common.Constants.CALENDAR_DAY_OF_MONTH_RANGE_END
import com.example.assemble_day.common.Constants.CALENDAR_DAY_OF_MONTH_RANGE_START
import com.example.assemble_day.common.Constants.FIRST_DAY_OF_MONTH
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_FOUR_HUNDRED_YEAR
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_FOUR_YEAR
import com.example.assemble_day.common.Constants.LEAP_YEAR_STANDARD_HUNDRED_YEAR
import com.example.assemble_day.common.Constants.SATURDAY_DAY_OF_WEEK
import com.example.assemble_day.common.Constants.SUNDAY_DAY_OF_WEEK
import com.example.assemble_day.domain.model.CalendarDay
import java.time.*

object CalendarUtil {

    /*fun getDayList(date: LocalDate): List<CalendarDay> {
         val year = date.year
         val month = date.month
         val firstDay = date.withDayOfMonth(1)
         val isLeapYear =
             (year % LEAP_YEAR_STANDARD_FOUR_YEAR == 0 && year % LEAP_YEAR_STANDARD_HUNDRED_YEAR != 0) || year % LEAP_YEAR_STANDARD_FOUR_HUNDRED_YEAR == 0
         val lastDay = month.length(isLeapYear)
         val firstDayOfWeek = firstDay.dayOfWeek.value

         val currentDate = LocalDate.now()
         val thisYear = currentDate.year
         val thisMonth = currentDate.monthValue
         val today = currentDate.dayOfMonth

         return createDayList(
             year = year,
             month = date.monthValue,
             firstDayOfWeek = firstDayOfWeek,
             lastDay = lastDay,
             today = today,
             thisMonth = thisMonth,
             thisYear = thisYear
         )
     }*/

    /*private fun createDayList(
        year: Int,
        month: Int,
        firstDayOfWeek: Int,
        lastDay: Int,
        today: Int,
        thisMonth: Int,
        thisYear: Int
    ): List<CalendarDay> {
        val dayList = mutableListOf<CalendarDay>()
        for (i in CALENDAR_DAY_OF_MONTH_RANGE_START..CALENDAR_DAY_OF_MONTH_RANGE_END) {
            if (i <= firstDayOfWeek || i > lastDay + firstDayOfWeek) {
                dayList.add(CalendarDay(year = year, month = month))
            } else {
                val day = i - firstDayOfWeek
                val isSaturday =
                    LocalDate.of(year, month, day).dayOfWeek.value == SATURDAY_DAY_OF_WEEK
                val isSunday =
                    LocalDate.of(year, month, day).dayOfWeek.value == SUNDAY_DAY_OF_WEEK
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
                            isSelectable = year > thisYear || (month > thisMonth && thisYear >= year),
                            isSaturday = isSaturday,
                            isSunday = isSunday
                        )
                    )
                }
            }
        }
        return dayList
    }*/

    fun createDayList(
        date: LocalDate
    ): List<CalendarDay> {
        val year = date.year
        val month = date.month
        val firstDay = date.withDayOfMonth(FIRST_DAY_OF_MONTH)
        val firstDayOfWeek = firstDay.dayOfWeek.value
        val isLeapYear =
            (year % LEAP_YEAR_STANDARD_FOUR_YEAR == 0 && year % LEAP_YEAR_STANDARD_HUNDRED_YEAR != 0) || year % LEAP_YEAR_STANDARD_FOUR_HUNDRED_YEAR == 0
        val lastDay = month.length(isLeapYear)

        val currentDate = LocalDate.now()
        val thisYear = currentDate.year
        val thisMonth = currentDate.monthValue
        val today = currentDate.dayOfMonth

        val dayList = mutableListOf<CalendarDay>()
        for (i in CALENDAR_DAY_OF_MONTH_RANGE_START..CALENDAR_DAY_OF_MONTH_RANGE_END) {
            if (i <= firstDayOfWeek || i > lastDay + firstDayOfWeek) {
                dayList.add(CalendarDay())
            } else {
                val day = i - firstDayOfWeek
                val newDate = LocalDate.of(year, month, day)
                val isSaturday = newDate.dayOfWeek.value == SATURDAY_DAY_OF_WEEK
                val isSunday = newDate.dayOfWeek.value == SUNDAY_DAY_OF_WEEK
                if (thisMonth == month.value) {
                    dayList.add(
                        CalendarDay(
                            date = newDate,
                            isSelectable = day >= today,
                            isSaturday = isSaturday,
                            isSunday = isSunday
                        )
                    )
                } else {
                    dayList.add(
                        CalendarDay(
                            date = newDate,
                            isSelectable = year > thisYear || (month.value > thisMonth && thisYear >= year),
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