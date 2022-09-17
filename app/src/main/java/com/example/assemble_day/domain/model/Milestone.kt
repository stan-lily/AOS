package com.example.assemble_day.domain.model

data class Milestone(
    val id: Int,
    val title: String,
    val progress: Int,
    val description: String,
    val date: String,
)
