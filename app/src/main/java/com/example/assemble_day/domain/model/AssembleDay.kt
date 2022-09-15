package com.example.assemble_day.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class AssembleDay(
    val id: Int = -1,
    @SerializedName("assembleTitle")
    val assembleTitle: String,
    @SerializedName("assembleStartAt")
    val assembleStartAt: String,
    @SerializedName("assembleEndAt")
    val assembleEndAt: String
) : Serializable
