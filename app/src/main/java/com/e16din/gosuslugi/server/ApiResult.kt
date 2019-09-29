package com.e16din.gosuslugi.server

import android.util.Log
import com.e16din.gosuslugi.server.data.services.ServicesData
import retrofit2.Response

typealias ApiResponse = Response<ServicesData>

sealed class ApiResult<out R> {

    data class Success<out R>(
        val responseCode: Int,
        val data: R?
    ) : ApiResult<R>()

    data class Error<out R>(
        val responseCode: Int,
        val error: String?,
        val message: String?,
        val data: Any?//todo:
    ) : ApiResult<R>()

    data class Fail(val throwable: Throwable) : ApiResult<Nothing>()
}

fun <T> Response<T>.awaitResult(): ApiResult<T> {
    return try {
        ApiResult.Success(code(), body())

        //todo: handle errors

    } catch (t: Throwable) {
        t.printStackTrace()
        Log.e("debug", "error.RequestManager: ${t.message}")
        ApiResult.Fail(t)
    }
}