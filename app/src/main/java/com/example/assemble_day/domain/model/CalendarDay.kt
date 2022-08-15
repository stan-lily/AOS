package com.example.assemble_day.domain.model

import java.time.LocalDate

//data class CalendarDay(
//    val year: Int,
//    val month: Int,
//    val day: String = "",
//    var isSelectable: Boolean = false,
//    val isSaturday: Boolean = false,
//    val isSunday: Boolean = false,
//    var isAssembleDay: Boolean = false,
//    var isStartDay: Boolean = false,
//)

data class CalendarDay(
    val date: LocalDate? = null,
    var isSelectable: Boolean = false,
    val isSaturday: Boolean = false,
    val isSunday: Boolean = false,
)