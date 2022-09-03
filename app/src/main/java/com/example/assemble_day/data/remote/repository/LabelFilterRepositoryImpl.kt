package com.example.assemble_day.data.remote.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dataSource.LabelDataSource
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.safeApiCall
import com.example.assemble_day.domain.repository.LabelFilterRepository
import javax.inject.Inject

class LabelFilterRepositoryImpl @Inject constructor(private val labelDataSource: LabelDataSource) :
    LabelFilterRepository {

    override suspend fun getLabel(): NetworkResult<LabelDto> {
        return labelDataSource.getLabel().safeApiCall()
    }

}