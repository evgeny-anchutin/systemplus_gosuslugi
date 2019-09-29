package com.e16din.gosuslugi.server.api

import com.e16din.gosuslugi.server.data.services.ServicesData
import com.e16din.gosuslugi.server.data.services.fields.ServiceFieldsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface CommonApi {

    /**
     *  Получить список возможных услуг.
     */
    @GET("GetServiceList")
    suspend fun loadServices(
        @Header("Authorization") authHeader: String,
        @Query("searchtext") query: String
    ): Response<ServicesData>

    /**
     *  Получить список полей для заявки.
     */
    @GET("GetField")
    suspend fun loadServiceFields(
        @Header("Authorization") authHeader: String,
        @Query("service_id") serviceId: Long
    ): Response<ServiceFieldsData>

    /**
     *  Отправить заявку.
     */
    @POST("Processing/StartService") //todo: try multipart (текстом в теле запроса)
    suspend fun sendRequest(
        @Header("Authorization") authHeader: String
    ): Response<String>
}