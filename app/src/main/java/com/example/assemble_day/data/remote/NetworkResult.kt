package com.example.assemble_day.data.remote

import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<out T: Any> {
    data class Success<T: Any>(val data: T) : NetworkResult<T>()
    data class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    data class Exception<T: Any>(val error: Throwable) : NetworkResult<T>()
}

fun <T: Any> Response<T>.safeApiCall(): NetworkResult<T> {
    return try {
        val response = this
        val responseBody = this.body()
        if (response.isSuccessful && responseBody != null) {
            NetworkResult.Success(responseBody)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}