package com.example.assemble_day.data.remote.repository

import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dataSource.LabelDataSource
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.safeApiCall
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.repository.LabelRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class LabelRepositoryImpl @Inject constructor(private val labelDataSource: LabelDataSource) :
    LabelRepository {

    override suspend fun getLabel(): NetworkResult<LabelDto> {
        return labelDataSource.getLabel().safeApiCall()
    }

    override suspend fun createLabel(newLabel: Label): NetworkResult<ResponseBody> {
        return labelDataSource.createLabel(newLabel).safeApiCall()
    }

    override suspend fun updateLabel(labelId: Int, updatingLabel: Label): NetworkResult<ResponseBody> {
        return labelDataSource.updateLabel(labelId, updatingLabel).safeApiCall()
    }

    override suspend fun deleteLabel(labelId: Int): NetworkResult<Unit> {
        return labelDataSource.deleteLabel(labelId).safeApiCall()
    }

}