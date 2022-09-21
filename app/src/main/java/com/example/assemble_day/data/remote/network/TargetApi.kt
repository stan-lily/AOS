package com.example.assemble_day.data.remote.network

import com.example.assemble_day.data.remote.dto.LabelDto
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.data.remote.dto.TargetCountsDto
import com.example.assemble_day.data.remote.dto.TargetDto
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface TargetApi {

    @GET("api/{team-name}/assembles/{id}")
    suspend fun getTargetsCount(@Path("team-name") name: String = "stan-lily", @Path("id") assembleId: Int): Response<TargetCountsDto>

    @GET("api/{team-name}/assembles/{id}/targets")
    suspend fun getTargets(@Path("team-name") teamName:String = "stan-lily", @Path("id") assembleId: Int, @Query("date") date: String): Response<TargetDto>

    @POST("api/{team-name}/assembles/{id}/targets")
    suspend fun createTarget(@Path("team-name") name: String = "stan-lily", @Path("id") assembleId: Int, @Body newTarget: PartDayTarget): Response<ResponseBody>

}