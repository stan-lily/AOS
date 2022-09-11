package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.TargetCountsDto
import com.example.assemble_day.data.remote.dto.TargetDto
import com.example.assemble_day.data.remote.network.LabelApi
import com.example.assemble_day.data.remote.network.TargetApi
import com.example.assemble_day.domain.model.PartDayTarget
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class TargetRemoteDataSource @Inject constructor(private val targetApi: TargetApi) : TargetDataSource {

    override suspend fun getTargetCounts(assembleId: Int): Response<TargetCountsDto> {
        return targetApi.getTargetsCount("stan-lily", assembleId)
    }

    override suspend fun getTargets(date: String): Response<TargetDto> {
        return targetApi.getTargets(date)
    }

    override suspend fun createTarget(
        assembleId: Int,
        newTarget: PartDayTarget
    ): Response<ResponseBody> {
        return targetApi.createTarget("stan-lily", assembleId, newTarget)
    }

}