package com.example.assemble_day.data.remote.network

import com.example.assemble_day.data.remote.dto.AssemblesDto
import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AssembleApi {

    @GET("api/{team-name}/assembles")
    suspend fun getAssembles(@Path("team-name") team: String): Response<AssemblesDto>

    @POST("api/stan-lily/issue-trackers/labels")
    suspend fun createLabel()

    @PUT("api/stan-lily/labels/1")
    suspend fun updateLabel()

    @DELETE("api/stan-lily/labels/1")
    suspend fun deleteLabel()


}