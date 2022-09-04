package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class AssemblesDto(
    @SerializedName("team")
    var team: Team,
    @SerializedName("assembles")
    var assembles: List<Assembles>
)

data class Assembles(
    @SerializedName("assemble_id")
    var assemble_id: Int,
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
        val localFormatError = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startLocalDate = LocalDate.parse(it.startAt, localFormatError)
        val endLocalDate = LocalDate.parse(it.endAt, localFormatError)
        assembleList.add(AssembleDay(it.title, startLocalDate, endLocalDate))
    }
    return assembleList
}