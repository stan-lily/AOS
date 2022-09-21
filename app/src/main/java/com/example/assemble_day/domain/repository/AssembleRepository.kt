package com.example.assemble_day.domain.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.Assembles
import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody

interface AssembleRepository {

    suspend fun getAssembles(): NetworkResult<AssemblesDto>
    suspend fun createAssembles(newAssembleDay: AssembleDay): NetworkResult<ResponseBody>
}