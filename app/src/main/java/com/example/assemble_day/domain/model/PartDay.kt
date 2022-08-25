package com.example.assemble_day.domain.model

import java.time.LocalDate

data class PartDay(
    val date: LocalDate,
    val assembleDay: AssembleDay,
    val targetList: List<PartDayTarget>
)
