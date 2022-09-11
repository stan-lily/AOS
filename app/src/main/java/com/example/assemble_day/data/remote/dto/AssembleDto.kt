package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class AssemblesDto(
    @SerializedName("team")
    val team: Team,
    @SerializedName("assembles")
    val assembles: List<Assembles>
)

data class Assembles(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String
)

fun AssemblesDto.toAssembleDay(): List<AssembleDay> {
    val assembleList = mutableListOf<AssembleDay>()
    this.assembles.forEach {
//        val localFormatError = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val startLocalDate = LocalDate.parse(it.startAt, localFormatError)
//        val endLocalDate = LocalDate.parse(it.endAt, localFormatError)
        assembleList.add(AssembleDay(it.id, it.title, it.startAt, it.endAt))
    }
    return assembleList
}