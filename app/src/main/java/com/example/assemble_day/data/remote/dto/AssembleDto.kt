package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.AssembleDay
import com.google.gson.annotations.SerializedName


data class AssemblesDto(
    @SerializedName("assembles")
    var assembles: List<Assembles>
)

data class Assembles(
    @SerializedName("id")
    var id: Int,
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
        assembleList.add(AssembleDay(it.id, it.title, it.startAt, it.endAt))
    }
    return assembleList
}

/*
fun List<Assembles>.toAssembleDay(): List<AssembleDay> {
    val assembleList = mutableListOf<AssembleDay>()
    this.forEach {
//        val localFormatError = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val startLocalDate = LocalDate.parse(it.startAt, localFormatError)
//        val endLocalDate = LocalDate.parse(it.endAt, localFormatError)
        assembleList.add(AssembleDay(it.id, it.title, it.startAt, it.endAt))
    }
    return assembleList
}
*/
