package com.example.assemble_day.domain.model

data class Issue(
    val title: String,
    val description: String,
    val label: Label,
    val milestone: String,
)
