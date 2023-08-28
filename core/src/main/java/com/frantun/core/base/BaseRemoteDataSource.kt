package com.frantun.core.base

import com.frantun.core.common.Resource
import retrofit2.Response

open class BaseRemoteDataSource {
    suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (exception: Exception) {
            return error(exception.message ?: exception.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.Error("Couldn't reach server for the following reason: $message")
    }
}
