package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response

interface LabelDataSource {
    suspend fun getLabel(): Response<LabelDto>
    suspend fun createLabel(newLabel: Label): Response<ResponseBody>
    suspend fun updateLabel(labelId: Int, updatingLabel: Label): Response<ResponseBody>
    suspend fun deleteLabel(labelId: Int): Response<Unit>
}