package com.example.assemble_day.domain.model

data class Issue(
    val id: Int,
    val title: String,
    val description: String,
    val label: Label,
    val milestone: String,
    var isClamped: Boolean = false
)
