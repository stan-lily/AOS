package com.example.assemble_day.data.remote.network

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.domain.model.Label
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface LabelApi {

    @GET("api/{team-name}/labels")
    suspend fun getLabel(@Path("team-name") teamName: String = "stan-lily"): Response<LabelDto>

    @POST("api/{team-name}/labels")
    suspend fun createLabel(@Path("team-name") teamName: String = "stan-lily", @Body newLabel: Label): Response<ResponseBody>

    @PUT("api/{team-name}/labels/{id}")
    suspend fun updateLabel(@Path("team-name") teamName: String = "stan-lily", @Path("id") labelId: Int, @Body updatingLabel: Label): Response<ResponseBody>

    @DELETE("api/{team-name}/labels/{id}")
    suspend fun deleteLabel(@Path("team-name") teamName: String = "stan-lily", @Path("id") labelId: Int): Response<Unit>


}