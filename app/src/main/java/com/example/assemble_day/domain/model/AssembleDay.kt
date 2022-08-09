package com.example.assemble_day.domain.model

import java.time.LocalDate

data class AssembleDay(
    val title: String,
    val start: LocalDate,
    val end: LocalDate
)
