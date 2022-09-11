package com.example.assemble_day.data.remote.dto

import com.example.assemble_day.domain.model.PartDay
import com.google.gson.annotations.SerializedName

data class TargetCountsDto(
    @SerializedName("targetCountsByDays")
    val targetCountsByDays: List<TargetCountsByDays>
)

data class TargetCountsByDays(
    @SerializedName("count")
    val count: Int,
    @SerializedName("date")
    val date: String
)


fun TargetCountsDto.toPartDayList(): List<PartDay> {
    val partDayList = mutableListOf<PartDay>()
    this.targetCountsByDays.forEach {
        partDayList.add(PartDay(count = it.count, date = it.date))
    }
    return partDayList
}