package com.example.assemble_day.domain.model

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: String = "",
    val isSelectable: Boolean = true,
    val isSaturday: Boolean = false,
    val isSunday: Boolean = false
)
