package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.Assembles
import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.network.AssembleApi
import com.example.assemble_day.domain.model.AssembleDay
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AssembleRemoteDataSource @Inject constructor(private val assembleApi: AssembleApi) : AssembleDataSource {

    override suspend fun getAssembles(): Response<AssemblesDto> {
        val response = assembleApi.getAssembles("stan-lily")
        println("로드함")
        println(response)
        return response
    }

    override suspend fun createAssembles(newAssembleDay: AssembleDay): Response<ResponseBody> {
        return assembleApi.createAssembles("stan-lily", newAssembleDay)
    }
}