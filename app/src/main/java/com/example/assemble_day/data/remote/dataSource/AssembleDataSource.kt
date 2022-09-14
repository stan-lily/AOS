package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.Assembles
import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response

interface AssembleDataSource {
    suspend fun getAssembles(): Response<AssemblesDto>
    suspend fun createAssembles(newAssembleDay: AssembleDay): Response<ResponseBody>
}