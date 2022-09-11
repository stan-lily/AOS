package com.example.assemble_day.domain.model

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class PartDayTarget(
//    val label: Label,
    val targetId: Int = -1,
    @SerializedName("labelId")
    val labelId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("targetAt")
    val targetAt: String,
    val isSelected: Boolean = false,
) : Serializable