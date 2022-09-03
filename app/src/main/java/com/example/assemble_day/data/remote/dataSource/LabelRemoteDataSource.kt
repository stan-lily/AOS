package com.example.assemble_day.data.remote.dataSource

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.network.LabelApi
import com.example.assemble_day.domain.model.Label
import retrofit2.Response
import javax.inject.Inject

class LabelRemoteDataSource @Inject constructor(private val labelApi: LabelApi) : LabelDataSource {

    override suspend fun getLabel(): Response<LabelDto> {
        return labelApi.getLabel()
    }

    override suspend fun createLabel(newLabel: Label) {
        labelApi.createLabel(newLabel).string()
    }

    override suspend fun updateLabel() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLabel() {
        TODO("Not yet implemented")
    }
}