package com.example.assemble_day.domain.model

import com.google.gson.annotations.SerializedName

data class Label(
    val id: Int = -1,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("backgroundColor")
    val backgroundColor: String,
    @SerializedName("fontColor")
    val fontColor: String,
    val isSelected: Boolean = false
)