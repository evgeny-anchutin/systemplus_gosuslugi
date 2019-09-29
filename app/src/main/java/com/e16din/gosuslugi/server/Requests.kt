package com.e16din.gosuslugi.server

import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.server.api.CommonApi
import kotlin.reflect.KClass


object Requests {

    private const val SERVER_URL = "http://83.221.208.29:3663/StateService/hs/StateService/"

    val commonApi: CommonApi by lazy {
        CommonApi::class.createApi()
    }

    private fun <T : Any> KClass<T>.createApi() =
        WebServiceFactory.retrofit(SERVER_URL).create(this.java)
}

fun ApiResult.Error<*>.handleApiError() {
    val text =
        if (message.isNullOrBlank()) "Сервер вернул ошибку, что-то пошло не так." else message
    android.widget.Toast.makeText(App.get, text, android.widget.Toast.LENGTH_LONG).show()
}