package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.Label
import com.google.gson.annotations.SerializedName

data class LabelDto(
    @SerializedName("team")
    val team: Team,
    @SerializedName("labels")
    val labels: List<Labels>
)

data class Labels(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("backgroundColor")
    val backgroundColor: String,
    @SerializedName("fontColor")
    val fontColor: String
)

data class Team(
    @SerializedName("teamId")
    val teamId: Int,
    @SerializedName("teamName")
    val teamName: String
)

fun LabelDto.toLabel(): List<Label> {
    val labelList = mutableListOf<Label>()
    this.labels.forEach {
        labelList.add(Label(it.id, it.name, it.description, it.backgroundColor, it.fontColor))
    }
    return labelList
}

