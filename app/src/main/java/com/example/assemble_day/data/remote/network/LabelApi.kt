package com.example.assemble_day.data.remote.network

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface LabelApi {

    @GET("api/stan-lily/labels")
    suspend fun getLabel(): Response<LabelDto>

    @POST("api/stan-lily/labels")
    suspend fun createLabel(@Body newLabel: Label): Response<ResponseBody>

    @PUT("api/stan-lily/labels/1")
    suspend fun updateLabel()

    @DELETE("api/stan-lily/labels/1")
    suspend fun deleteLabel()


}