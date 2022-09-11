package com.example.assemble_day.domain.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response

interface LabelRepository {

    suspend fun getLabel(): NetworkResult<LabelDto>
    suspend fun createLabel(newLabel: Label): NetworkResult<ResponseBody>

}