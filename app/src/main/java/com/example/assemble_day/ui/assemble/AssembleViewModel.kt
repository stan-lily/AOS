package com.example.assemble_day.ui.assemble

import androidx.lifecycle.ViewModel
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MAX_SELECTABLE_WEEK
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MIN_SELECTABLE_DAY
import com.example.assemble_day.common.Constants.CALENDAR_DAY_SIZE
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.ui.common.CalendarUtil
import com.example.assemble_day.ui.common.CalendarUtil.toFormattedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class AssembleViewModel : ViewModel() {

    private val initialCalendarDay = CalendarDay()
    private var assembleDayTitle = ""

    private val _calendarDataMap = mutableMapOf<String, List<CalendarDay>>()
    val calendarDataMap: Map<String, List<CalendarDay>> = _calendarDataMap

    private val _resetActionFlagStateFlow = MutableStateFlow(false)
    val resetActionFlagStateFlow = _resetActionFlagStateFlow.asStateFlow()

    private val _assembleDayStateFlow = MutableStateFlow(initialCalendarDay)
    val assembleDayStateFlow = _assembleDayStateFlow.asStateFlow()

    private val _copiedCalendarDayList = mutableListOf<CalendarDay>()
    val copiedCalendarDayList: List<CalendarDay> = _copiedCalendarDayList

    private val _startDayStateFlow = MutableStateFlow(initialCalendarDay)
    val startDayStateFlow = _startDayStateFlow.asStateFlow()

    private val _startDayFlagStateFlow = MutableStateFlow(true)
    val startDayFlagStateFlow = _startDayFlagStateFlow.asStateFlow()

    init {
        createCalendarData()
    }

    private fun createCalendarData() {
        val calendarData =
            Array<LocalDate>(CALENDAR_DAY_SIZE) { LocalDate.now().plusMonths(it.toLong()) }
        calendarData.forEach { date ->
            _calendarDataMap[date.toFormattedString()] = CalendarUtil.createDayList(date)
        }
    }

    private fun loadAssembleDay() {

    }

    fun setAssembleDayTitle(title: String) {
        assembleDayTitle = title
    }

    fun selectCalendarDay(calendarDay: CalendarDay) {
        _startDayFlagStateFlow.value = _startDayStateFlow.value.date == null
        if (_startDayFlagStateFlow.value) {
            selectStartDay(calendarDay)
        } else {
            selectAssembleDay(calendarDay)
        }
    }

    private fun selectAssembleDay(calendarDay: CalendarDay) {
        _startDayStateFlow.value.date?.let { startDay ->
            val selectableMinDay = startDay.plusDays(ASSEMBLE_DAY_MIN_SELECTABLE_DAY)
            if (selectableMinDay <= calendarDay.date) {
                val selectedDay = calendarDay.toFormattedString()
                val previousSelectedDay = _assembleDayStateFlow.value
                _copiedCalendarDayList.removeAll(_copiedCalendarDayList)
                _calendarDataMap[selectedDay]?.forEach { originalDay ->
                    when (originalDay) {
                        calendarDay -> _copiedCalendarDayList.add(originalDay.copy(isAssembleDay = true))
                        previousSelectedDay -> _copiedCalendarDayList.add(
                            originalDay.copy(
                                isAssembleDay = false
                            )
                        )
                        else -> _copiedCalendarDayList.add(originalDay.copy())
                    }
                }
                _assembleDayStateFlow.value = calendarDay
                _resetActionFlagStateFlow.value = true
            } else {
                _startDayFlagStateFlow.value = true
            }
        }
    }

    private fun selectStartDay(calendarDay: CalendarDay) {
        if (_startDayStateFlow.value.date == null) {
            _resetActionFlagStateFlow.value = true
            calendarDay.date?.let { selectedDay ->
                val selectableMaxDay = selectedDay.plusWeeks(ASSEMBLE_DAY_MAX_SELECTABLE_WEEK)
                _calendarDataMap.values.forEach { dateList ->
                    dateList.forEach { calendarDay ->
                        calendarDay.date?.let { day ->
                            if (day < selectedDay || day >= selectableMaxDay)
                                calendarDay.isSelectable = false
                        }
                    }
                }
            }
            _startDayStateFlow.value = calendarDay
        }
    }

    private fun resetCalendarData() {
        val today = LocalDate.now()
        _calendarDataMap.forEach { (_, dateList) ->
            dateList.forEach { calendarDay ->
                calendarDay.date?.let { day ->
                    if (day >= today) calendarDay.isSelectable = true
                }
            }
        }
    }

    fun resetAssembleDay() {
        _assembleDayStateFlow.value.isAssembleDay = false
        _assembleDayStateFlow.value = initialCalendarDay
        _startDayStateFlow.value = initialCalendarDay
        _resetActionFlagStateFlow.value = false
        _startDayFlagStateFlow.value = true
        assembleDayTitle = ""
        resetCalendarData()
    }


}