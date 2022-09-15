package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.PartDayTarget
import com.google.gson.annotations.SerializedName

data class TargetDto(
    @SerializedName("targets")
    var targets: List<Targets>
)

data class Targets(
    @SerializedName("labelBackgroundColor")
    val labelBackgroundColor: String,
    @SerializedName("labelId")
    var labelId: Int,
    @SerializedName("targetAt")
    val targetAt: String,
    @SerializedName("targetId")
    var targetId: Int,
    @SerializedName("targetTitle")
    val targetTitle: String
)

//data class Targets(
//    @SerializedName("targetId")
//    val targetId: Int,
//    @SerializedName("targetAt")
//    val targetAt: String,
//    @SerializedName("labelId")
//    val labelId: Int,
//    val title: String
//)

fun TargetDto.toPartDayTarget(): List<PartDayTarget> {
    val targetList = mutableListOf<PartDayTarget>()
    this.targets.forEach {
        targetList.add(PartDayTarget(
            labelBackgroundColor = it.labelBackgroundColor,
            targetId = it.targetId,
            labelId = it.labelId,
            title = it.targetTitle,
            targetAt = it.targetAt)
        )
    }
    return targetList
}