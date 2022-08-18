package com.example.assemble_day.ui.assemble

import androidx.lifecycle.ViewModel
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MAX_SELECTABLE_WEEK
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MIN_SELECTABLE_DAY
import com.example.assemble_day.common.Constants.CALENDAR_DAY_SIZE
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.ui.common.CalendarUtil
import com.example.assemble_day.ui.common.CalendarUtil.toFormattedString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class AssembleViewModel : ViewModel() {

    private val initialCalendarDay = CalendarDay()
    private val assembleDayList = mutableListOf<AssembleDay>()

    private var _calendarDataMap = mutableMapOf<String, List<CalendarDay>>()
    val calendarDataMap: Map<String, List<CalendarDay>> = _calendarDataMap

    private val _assembleDayTitleStateFlow = MutableStateFlow("")
    val assembleDayTitleStateFlow = _assembleDayTitleStateFlow.asStateFlow()

    private val _assembleDayStateFlow = MutableStateFlow(initialCalendarDay)
    val assembleDayStateFlow = _assembleDayStateFlow.asStateFlow()

    private val _copiedCalendarDayList = mutableListOf<CalendarDay>()
    val copiedCalendarDayList: List<CalendarDay> = _copiedCalendarDayList

    private var _isSelectedDifferentMonth = false
    val isSelectedDifferentMonth: Boolean get() = _isSelectedDifferentMonth

    private var _previousAssembleDay = initialCalendarDay
    val previousAssembleDay: CalendarDay get() = _previousAssembleDay

    private val _copiedPreviousCalendarDayList = mutableListOf<CalendarDay>()
    val copiedPreviousCalendarDayList: List<CalendarDay> = _copiedPreviousCalendarDayList

    private val _startDayStateFlow = MutableStateFlow(initialCalendarDay)
    val startDayStateFlow = _startDayStateFlow.asStateFlow()

    private val _selectableAssembleDayFlagSharedFlow = MutableSharedFlow<Boolean>(replay = 1)
    val selectableAssembleDayFlagSharedFlow =
        _selectableAssembleDayFlagSharedFlow.asSharedFlow()

    init {
        createCalendarData()
        loadAssembleDays()
        setLoadedAssembleDays()
    }

    private fun createCalendarData() {
        val calendarData =
            Array<LocalDate>(CALENDAR_DAY_SIZE) { LocalDate.now().plusMonths(it.toLong()) }
        calendarData.forEach { date ->
            _calendarDataMap[date.toFormattedString()] = CalendarUtil.createDayList(date)
        }
    }

    private fun loadAssembleDays() {
        assembleDayList.removeAll(assembleDayList)
        val dummyAssembleDayList = listOf<AssembleDay>(
//            AssembleDay("기획 2차 수정", LocalDate.of(2022, 8, 1), LocalDate.of(2022, 8, 5)),
//            AssembleDay("레이블 기능", LocalDate.of(2022, 8, 8), LocalDate.of(2022, 8, 17)),
//            AssembleDay("assembles", LocalDate.of(2022, 8, 19), LocalDate.of(2022, 8, 26)),
        )
        assembleDayList.addAll(dummyAssembleDayList)
    }

    private fun setLoadedAssembleDays() {
        assembleDayList.forEach { assembleDay ->
            val loadedStartDay = assembleDay.start
            val loadedStartDayMonth = loadedStartDay.toFormattedString()
            val loadedAssembleDay = assembleDay.end
            val loadedAssembleDayMonth = loadedAssembleDay.toFormattedString()
            val startDayAfterLastAssembleDay = assembleDayList.last().end.plusDays(1)

            if (loadedStartDayMonth < loadedAssembleDayMonth) {
                _calendarDataMap[loadedStartDayMonth]?.forEach { calendarDay ->
                    calendarDay.date?.let { originalDay ->
                        if (originalDay >= loadedStartDay) calendarDay.isSelectable = false
                    }
                }
            }
            _calendarDataMap[loadedAssembleDayMonth]?.forEach { calendarDay ->
                calendarDay.date?.let { originalDay ->
                    if (originalDay >= loadedStartDay && originalDay < loadedAssembleDay) {
                        calendarDay.isSelectable = false
                    }
                    if (originalDay == loadedAssembleDay) {
                        calendarDay.isAssembleDay = true
                        calendarDay.assembleDay = assembleDay
                    }
                    if (originalDay == startDayAfterLastAssembleDay) {
                        selectStartDay(calendarDay)
                    }
                }
            }
        }
    }

    fun setAssembleDayTitle(title: String) {
        _assembleDayTitleStateFlow.value = title
    }

    fun selectCalendarDay(calendarDay: CalendarDay) {
        if (calendarDay.isAssembleDay) {
            _assembleDayStateFlow.value = calendarDay
            setAssembleDayTitle(calendarDay.assembleDay?.title ?: "")
            calendarDay.assembleDay?.let { assembleDay ->
                _startDayStateFlow.value = CalendarDay(date = assembleDay.start)
            }
        } else if (_startDayStateFlow.value == initialCalendarDay) {
            selectStartDay(calendarDay)
        } else {
            selectAssembleDay(calendarDay)
        }
    }

    private fun selectAssembleDay(selectedDay: CalendarDay) {
        _startDayStateFlow.value.date?.let { startDay ->
            val selectableMinDay = startDay.plusDays(ASSEMBLE_DAY_MIN_SELECTABLE_DAY)
            if (selectableMinDay <= selectedDay.date) {
                val selectedMonth = selectedDay.toFormattedString()
                val previousSelectedDay = _assembleDayStateFlow.value
                val previousSelectedMonth = previousSelectedDay.toFormattedString()
                _isSelectedDifferentMonth = false

                if (selectedMonth != previousSelectedMonth) {
                    selectAssembleDayWithDifferentMonth(previousSelectedDay, previousSelectedMonth)
                }
                selectAssembleDayWithInSameMonth(selectedMonth, selectedDay, previousSelectedDay)
                _assembleDayStateFlow.value = selectedDay
                _selectableAssembleDayFlagSharedFlow.tryEmit(true)
            } else {
                _selectableAssembleDayFlagSharedFlow.tryEmit(false)
            }
        }
    }

    private fun selectAssembleDayWithInSameMonth(
        selectedMonth: String,
        selectedDay: CalendarDay,
        previousSelectedDay: CalendarDay
    ) {
        _copiedCalendarDayList.removeAll(_copiedCalendarDayList)
        _calendarDataMap[selectedMonth]?.forEach { originalDay ->
            when (originalDay) {
                selectedDay -> _copiedCalendarDayList.add(originalDay.copy(isSelected = true))
                previousSelectedDay -> _copiedCalendarDayList.add(
                    originalDay.copy(
                        isSelected = false
                    )
                )
                else -> _copiedCalendarDayList.add(originalDay.copy())
            }
        }
    }

    private fun selectAssembleDayWithDifferentMonth(
        previousSelectedDay: CalendarDay,
        previousSelectedMonth: String
    ) {
        _isSelectedDifferentMonth = true
        _previousAssembleDay = previousSelectedDay
        _copiedPreviousCalendarDayList.removeAll(_copiedPreviousCalendarDayList)
        _calendarDataMap[previousSelectedMonth]?.forEach { originalDay ->
            when (originalDay) {
                previousSelectedDay -> _copiedPreviousCalendarDayList.add(
                    originalDay.copy(
                        isSelected = false
                    )
                )
                else -> _copiedPreviousCalendarDayList.add(originalDay.copy())
            }
        }
    }

    private fun selectStartDay(calendarDay: CalendarDay) {
        if (_startDayStateFlow.value == initialCalendarDay) {
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

    private fun resetCalendarDataWhenAssembleDayListEmpty() {
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
        _assembleDayStateFlow.value = initialCalendarDay
        _startDayStateFlow.value = initialCalendarDay
        _selectableAssembleDayFlagSharedFlow.tryEmit(true)
        _assembleDayTitleStateFlow.value = ""
        if (assembleDayList.isEmpty()) resetCalendarDataWhenAssembleDayListEmpty()
        else setLoadedAssembleDays()
    }

    fun saveAssembleDay() {
        // TODO 설정한 AssembleDay 저장 로직
    }

}