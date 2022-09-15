package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.TargetCountsDto
import com.example.assemble_day.data.remote.dto.TargetDto
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.LocalDate

interface TargetDataSource {
    suspend fun getTargetCounts(assembleId: Int): Response<TargetCountsDto>
    suspend fun getTargets(assembleId: Int, date: String): Response<TargetDto>
    suspend fun createTarget(assembleId: Int, newTarget: PartDayTarget): Response<ResponseBody>
}