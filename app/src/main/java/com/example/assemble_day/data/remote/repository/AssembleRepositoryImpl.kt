package com.example.assemble_day.data.remote.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dataSource.AssembleDataSource
import com.example.assemble_day.data.remote.dataSource.LabelDataSource
import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.safeApiCall
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.repository.AssembleRepository
import com.example.assemble_day.domain.repository.LabelRepository
import javax.inject.Inject

class AssembleRepositoryImpl @Inject constructor(private val assembleDataSource: AssembleDataSource) :
    AssembleRepository {

    override suspend fun getAssembles(): NetworkResult<AssemblesDto> {
        return assembleDataSource.getAssembles().safeApiCall()
    }

}