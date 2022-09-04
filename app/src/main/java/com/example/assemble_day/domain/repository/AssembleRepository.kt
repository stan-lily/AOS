package com.example.assemble_day.domain.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.Label

interface AssembleRepository {

    suspend fun getAssembles(): NetworkResult<AssemblesDto>

}