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

    @GET("api/{team-name}/asembles/{id}")
    suspend fun getTargetsCount(@Path("team-name") name: String = "stan-lily", @Path("id") assembleId: Int): Response<TargetCountsDto>

    @GET("api/stan-lily/assembles/1/targets?targetAt=2022-07-01")
    suspend fun getTargets(@Query("date") date: String): Response<TargetDto>

    @POST("api/{team-name}/asembles/{id}/targets")
    suspend fun createTarget(@Path("team-name") name: String = "stan-lily", @Path("id") assembleId: Int, @Body newTarget: PartDayTarget): Response<ResponseBody>

}