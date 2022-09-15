package com.example.assemble_day.domain.model

import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class PartDayTarget(
    @SerializedName("targetId")
    val targetId: Int = -1,
    @SerializedName("labelBackgroundColor")
    val labelBackgroundColor: String = LABEL_FONT_COLOR_BLACK_HEX,
    @SerializedName("labelId")
    val labelId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("targetAt")
    val targetAt: String,
    val isSelected: Boolean = false,
) : Serializable