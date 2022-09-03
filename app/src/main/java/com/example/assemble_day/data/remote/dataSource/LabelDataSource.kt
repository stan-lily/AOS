package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.LabelDto
import retrofit2.Response

interface LabelDataSource {
    suspend fun getLabel(): Response<LabelDto>
    suspend fun createLabel()
    suspend fun updateLabel()
    suspend fun deleteLabel()
}