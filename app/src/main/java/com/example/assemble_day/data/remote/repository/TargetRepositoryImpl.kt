package com.example.assemble_day.data.remote.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dataSource.TargetDataSource
import com.example.assemble_day.data.remote.dto.TargetCountsDto
import com.example.assemble_day.data.remote.dto.TargetDto
import com.example.assemble_day.data.remote.safeApiCall
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.repository.TargetRepository
import okhttp3.ResponseBody
import java.time.LocalDate
import javax.inject.Inject

class TargetRepositoryImpl @Inject constructor(private val targetDataSource: TargetDataSource): TargetRepository {
    override suspend fun getTargetCounts(assembleId: Int): NetworkResult<TargetCountsDto> {
        return targetDataSource.getTargetCounts(assembleId).safeApiCall()
    }

    override suspend fun getTargets(date: String): NetworkResult<TargetDto> {
        return targetDataSource.getTargets(date).safeApiCall()
    }

    override suspend fun createTarget(
        assembleId: Int,
        newTarget: PartDayTarget
    ): NetworkResult<ResponseBody> {
        return targetDataSource.createTarget(assembleId, newTarget).safeApiCall()
    }

}