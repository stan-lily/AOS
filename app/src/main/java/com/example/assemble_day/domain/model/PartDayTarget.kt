package com.example.assemble_day.domain.model

import java.io.Serializable

data class PartDayTarget(
    val label: Label,
    val title: String,
    val isSelected: Boolean = false,
) : Serializable