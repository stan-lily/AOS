package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.domain.model.Label
import retrofit2.Response

interface LabelDataSource {
    suspend fun getLabel(): Response<LabelDto>
    suspend fun createLabel(newLabel: Label)
    suspend fun updateLabel()
    suspend fun deleteLabel()
}