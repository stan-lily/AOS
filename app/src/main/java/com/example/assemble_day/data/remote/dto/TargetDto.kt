package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.PartDayTarget
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TargetDto(
    @SerializedName("targets")
    var targets: List<Targets>
)

data class Targets(
    @SerializedName("targetId")
    val targetId: Int,
    @SerializedName("targetAt")
    val targetAt: String,
    @SerializedName("labelId")
    val labelId: Int,
    val title: String
)


fun TargetDto.toPartDayTarget(): List<PartDayTarget> {
    val targetList = mutableListOf<PartDayTarget>()
    this.targets.forEach {
        targetList.add(PartDayTarget(targetId = it.targetId, labelId = it.labelId, title = it.title, targetAt = it.targetAt))
    }
    return targetList
}