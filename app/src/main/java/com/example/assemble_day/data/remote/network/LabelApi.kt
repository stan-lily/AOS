package com.example.assemble_day.data.remote.network

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.domain.model.Label
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface LabelApi {

    @GET("api/stan-lily/issue-trackers/labels")
    suspend fun getLabel(): Response<LabelDto>

    @POST("api/stan-lily/labels")
    suspend fun createLabel()

    @PUT("api/stan-lily/labels/1")
    suspend fun updateLabel()

    @DELETE("api/stan-lily/labels/1")
    suspend fun deleteLabel()


}