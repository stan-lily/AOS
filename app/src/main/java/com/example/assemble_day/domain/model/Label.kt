package com.example.assemble_day.domain.model

data class Label(
    val id: Int = -1,
    val name: String,
    val description: String,
    val backgroundColor: String,
    val fontColor: String,
    val isSelected: Boolean = false
)
