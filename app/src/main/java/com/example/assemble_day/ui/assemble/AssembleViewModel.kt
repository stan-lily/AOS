package com.example.assemble_day.ui.assemble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MAX_SELECTABLE_WEEK
import com.example.assemble_day.common.Constants.ASSEMBLE_DAY_MIN_SELECTABLE_DAY
import com.example.assemble_day.common.Constants.CALENDAR_DAY_SIZE
import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.toAssembleDay
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.domain.repository.AssembleRepository
import com.example.assemble_day.ui.common.CalendarUtil
import com.example.assemble_day.ui.common.toLocalDateFormat
import com.example.assemble_day.ui.common.toStringFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AssembleViewModel @Inject constructor(private val assembleRepository: AssembleRepository) :
    ViewModel() {

    private val defaultCalendarDay = CalendarDay()
    private val defaultCalendarDayMap = mutableMapOf<LocalDate, List<CalendarDay>>()
    private val assembleDayList = mutableListOf<AssembleDay>()

    private var _calendarDataMapStateFlow = MutableStateFlow(mapOf<LocalDate, List<CalendarDay>>())
    val calendarDataMapStateFlow = _calendarDataMapStateFlow.asStateFlow()

    private val _assembleDayTitleStateFlow = MutableStateFlow("")
    val assembleDayTitleStateFlow = _assembleDayTitleStateFlow.asStateFlow()

    private val _assembleDayStateFlow = MutableStateFlow(defaultCalendarDay)
    val assembleDayStateFlow = _assembleDayStateFlow.asStateFlow()

    private var _didPreviouslySelectAssembleDay = false

    private val _startDayStateFlow = MutableStateFlow(defaultCalendarDay)
    val startDayStateFlow = _startDayStateFlow.asStateFlow()

    private val _selectableAssembleDayFlagSharedFlow = MutableSharedFlow<Boolean>(replay = 1)
    val selectableAssembleDayFlagSharedFlow =
        _selectableAssembleDayFlagSharedFlow.asSharedFlow()

    private val ceh = CoroutineExceptionHandler { _, throwable ->
        println(throwable)
    }

    init {
        viewModelScope.launch {
            loadAssembleDays()
        }
    }

    /*private fun loadAssembleDays() {
        assembleDayList.clear()
        val dummyAssembleDayList = listOf<AssembleDay>(
            AssembleDay(1, "기획 2차 수정", "2022-09-01", "2022-09-05"),
            AssembleDay(2, "레이블 기능", "2022-09-14", "2022-09-17"),
            AssembleDay(3, "assembles", "2022-09-22", "2022-09-30"),
        )
        assembleDayList.addAll(dummyAssembleDayList)
        createCalendarData()
    }*/

    private suspend fun loadAssembleDays() {
        assembleDayList.clear()
        val loadedAssemble = assembleRepository.getAssembles()
        when (loadedAssemble) {
            is NetworkResult.Success -> {
                assembleDayList.addAll(loadedAssemble.data.toAssembleDay())
                println(assembleDayList)
                createCalendarData()
            }
            else -> {
            }
        }
    }

    private fun createCalendarData() {
        defaultCalendarDayMap.clear()
        val calendarDateMap = mutableMapOf<LocalDate, List<CalendarDay>>()
        val calendarData =
            Array<LocalDate>(CALENDAR_DAY_SIZE) { LocalDate.now().plusMonths(it.toLong()) }
        calendarData.forEach { date ->
            calendarDateMap[date] = CalendarUtil.createDayList(date, assembleDayList)
        }
        _calendarDataMapStateFlow.value = calendarDateMap
        defaultCalendarDayMap.putAll(calendarDateMap)
    }

//    private fun setLoadedAssembleDays() {
//        assembleDayList.forEach { assembleDay ->
//            val loadedStartDay = assembleDay.start
//            val loadedStartDayMonth = loadedStartDay.toFormattedString()
//            val loadedAssembleDay = assembleDay.end
//            val loadedAssembleDayMonth = loadedAssembleDay.toFormattedString()
//            val startLocalDateAfterLastAssembleDay = assembleDayList.last().end.plusDays(1)
//            startDayAfterLastAssembleDay = CalendarDay(date = startLocalDateAfterLastAssembleDay)
//
//            if (loadedStartDayMonth < loadedAssembleDayMonth) {
//                _calendarDataMapStateFlow[loadedStartDayMonth]?.forEach { calendarDay ->
//                    calendarDay.date?.let { originalDay ->
//                        if (originalDay >= loadedStartDay) calendarDay.isSelectable = false
//                    }
//                }
//            }
//            _calendarDataMapStateFlow[loadedAssembleDayMonth]?.forEach { calendarDay ->
//                calendarDay.date?.let { originalDay ->
//                    if (originalDay >= loadedStartDay && originalDay < loadedAssembleDay) {
//                        calendarDay.isSelectable = false
//                    }
//                    if (originalDay == loadedAssembleDay) {
//                        calendarDay.isAssembleDay = true
//                        calendarDay.assembleDay = assembleDay
//                    }
//                    if (originalDay == startLocalDateAfterLastAssembleDay) {
//                        selectStartDay(calendarDay)
//                    }
//                }
//            }
//        }
//    }

    fun setAssembleDayTitle(title: String) {
        _assembleDayTitleStateFlow.value = title
    }

    fun selectCalendarDay(calendarDay: CalendarDay) {
        if (calendarDay.isAssembleDay) {
            selectLoadedAssembleDay(calendarDay)
        } else if (_startDayStateFlow.value == defaultCalendarDay || _didPreviouslySelectAssembleDay) {
            selectStartDay(calendarDay)
        } else {
            selectAssembleDay(calendarDay)
        }
        /*} else if (_startDayStateFlow.value == defaultCalendarDay) {
            selectStartDay(calendarDay)
        } else {
            if (didPreviouslySelectAssembleDay) {
                _startDayStateFlow.value = startDayAfterLastAssembleDay
                _didPreviouslySelectAssembleDay = false
            }
            selectAssembleDay(calendarDay)
        }*/
    }

    private fun selectStartDay(calendarDay: CalendarDay) {
        if (assembleDayList.isEmpty()) {
            selectStartDayWithoutLoadedAssembleDay(calendarDay)
        } else {
            selectStartDayWithLoadedAssembleDay(calendarDay)
        }
    }

    private fun selectStartDayWithLoadedAssembleDay(calendarDay: CalendarDay) {
        _didPreviouslySelectAssembleDay = false
        setAssembleDayTitle("")
        _assembleDayStateFlow.value = defaultCalendarDay
        _startDayStateFlow.value = calendarDay
    }

    private fun selectStartDayWithoutLoadedAssembleDay(calendarDay: CalendarDay) {
        calendarDay.date?.let { selectedDate ->
            val selectableMaxDay = selectedDate.plusWeeks(ASSEMBLE_DAY_MAX_SELECTABLE_WEEK)
            val calendarDayMap = _calendarDataMapStateFlow.value.toMutableMap()
            calendarDayMap.forEach { (localDateKey, calendarDayList) ->
                val updatedCalendarDayList = calendarDayList.toMutableList()
                updatedCalendarDayList.forEachIndexed { index, calendarDay ->
                    calendarDay.date?.let { newDate ->
                        if (newDate < selectedDate || newDate > selectableMaxDay) {
                            updatedCalendarDayList[index] =
                                updatedCalendarDayList[index].copy(isSelectable = false)
                        }
                    }
                }
                calendarDayMap[localDateKey] = updatedCalendarDayList
            }
            _calendarDataMapStateFlow.value = calendarDayMap.toMap()
            _startDayStateFlow.value = calendarDay
        }
    }

    private fun selectLoadedAssembleDay(calendarDay: CalendarDay) {
        _assembleDayStateFlow.value = calendarDay
        setAssembleDayTitle(calendarDay.assembleDay?.assembleTitle ?: "")
        calendarDay.assembleDay?.let { assembleDay ->
            _startDayStateFlow.value =
                CalendarDay(date = assembleDay.assembleStartAt.toLocalDateFormat())
        }
        _didPreviouslySelectAssembleDay = true
    }

    private fun selectAssembleDay(selectedDay: CalendarDay) {
        _startDayStateFlow.value.date?.let { startDay ->
            val selectableMinDay = startDay.plusDays(ASSEMBLE_DAY_MIN_SELECTABLE_DAY)
            if (selectableMinDay <= selectedDay.date) {
                _assembleDayStateFlow.value = selectedDay
                _selectableAssembleDayFlagSharedFlow.tryEmit(true)
            } else {
                _selectableAssembleDayFlagSharedFlow.tryEmit(false)
            }
        }
    }

    /*private fun selectAssembleDay(selectedDay: CalendarDay) {
        _startDayStateFlow.value.date?.let { startDay ->
            val selectableMinDay = startDay.plusDays(ASSEMBLE_DAY_MIN_SELECTABLE_DAY)
            if (selectableMinDay <= selectedDay.date) {
                val selectedMonth = selectedDay.toFormattedString()
                val previousSelectedDay = _assembleDayStateFlow.value
                val previousSelectedMonth = previousSelectedDay.toFormattedString()
                _isSelectedDifferentMonth = false

                if (selectedDay.date == previousSelectedDay.date) return
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
    }*/

    /*private fun selectAssembleDayWithInSameMonth(
        selectedMonth: String,
        selectedDay: CalendarDay,
        previousSelectedDay: CalendarDay
    ) {
        _copiedCalendarDayList.clear()
        _calendarDataMapStateFlow[selectedMonth]?.forEach { originalDay ->
            when (originalDay) {
                selectedDay -> _copiedCalendarDayList.add(originalDay.copy(isSelected = true))
                previousSelectedDay -> {
                    _copiedCalendarDayList.add(
                        originalDay.copy(
                            isSelected = false
                        )
                    )
                }
                else -> _copiedCalendarDayList.add(originalDay.copy())
            }
        }
    }*/

    /* private fun selectAssembleDayWithDifferentMonth(
         previousSelectedDay: CalendarDay,
         previousSelectedMonth: String
     ) {
         _isSelectedDifferentMonth = true
         _previousAssembleDay = previousSelectedDay
         _copiedPreviousCalendarDayList.clear()
         _calendarDataMapStateFlow[previousSelectedMonth]?.forEach { originalDay ->
             when (originalDay) {
                 previousSelectedDay -> _copiedPreviousCalendarDayList.add(
                     originalDay.copy(
                         isSelected = false
                     )
                 )
                 else -> _copiedPreviousCalendarDayList.add(originalDay.copy())
             }
         }
     }*/

    /*private fun selectStartDay(calendarDay: CalendarDay) {
           calendarDay.date?.let { selectedDay ->
               val selectableMaxDay = selectedDay.plusWeeks(ASSEMBLE_DAY_MAX_SELECTABLE_WEEK)
               _calendarDataMapStateFlow.values.forEach { dateList ->
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
    }*/

    /*private fun resetCalendarDataWhenAssembleDayListEmpty() {
        val today = LocalDate.now()
        _calendarDataMapStateFlow.forEach { (_, dateList) ->
            dateList.forEach { calendarDay ->
                calendarDay.date?.let { day ->
                    if (day >= today) calendarDay.isSelectable = true
                }
            }
        }
    }*/

    fun resetAssembleDay() {
        _assembleDayStateFlow.value = defaultCalendarDay
        _startDayStateFlow.value = defaultCalendarDay
        _selectableAssembleDayFlagSharedFlow.tryEmit(true)
        _didPreviouslySelectAssembleDay = false
        _assembleDayTitleStateFlow.value = ""
        _calendarDataMapStateFlow.value = defaultCalendarDayMap
//        if (assembleDayList.isEmpty()) resetCalendarDataWhenAssembleDayListEmpty()
//        else setLoadedAssembleDays()
    }

    fun saveAssembleDay() {
        viewModelScope.launch {
            if (_startDayStateFlow.value.date != null && _assembleDayStateFlow.value.date != null) {
                val newAssembleDay =
                    AssembleDay(
                        -1,
                        _assembleDayTitleStateFlow.value,
                        _startDayStateFlow.value.date?.toStringFormat() ?: "",
                        _assembleDayStateFlow.value.date?.toStringFormat() ?: ""
                    )
                println(newAssembleDay)
                val requestCreateAssemble = assembleRepository.createAssembles(newAssembleDay)
                when (requestCreateAssemble) {
                    is NetworkResult.Success -> {
                        loadAssembleDays()
                        resetAssembleDay()
                    }
                    is NetworkResult.Error -> {
                        println(requestCreateAssemble.code)
                        println(requestCreateAssemble.message)
                    }
                    is NetworkResult.Exception -> {
                        println(requestCreateAssemble.error)
                    }
                }
            }
        }
    }

}