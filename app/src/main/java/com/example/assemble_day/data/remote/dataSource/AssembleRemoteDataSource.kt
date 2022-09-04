package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.network.AssembleApi
import retrofit2.Response
import javax.inject.Inject

class AssembleRemoteDataSource @Inject constructor(private val assembleApi: AssembleApi) : AssembleDataSource {

    override suspend fun getAssembles(): Response<AssemblesDto> {
        return assembleApi.getAssembles("stan-lily")
    }
}