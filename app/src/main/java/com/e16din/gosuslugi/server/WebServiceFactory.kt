package com.e16din.gosuslugi.server

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WebServiceFactory {

    fun retrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = createLoggingInterceptor()
        val interceptors = arrayOf<Interceptor>(loggingInterceptor)
        val httpClient = createOkHttpClient(interceptors)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return retrofit
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private fun createOkHttpClient(interceptors: Array<Interceptor>): OkHttpClient.Builder {
        val build = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        interceptors.forEach {
            build.addInterceptor(it)
        }

        return build
    }
}