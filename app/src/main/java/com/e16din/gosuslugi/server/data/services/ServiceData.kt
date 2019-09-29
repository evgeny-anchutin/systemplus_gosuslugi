package com.e16din.gosuslugi.server.data.services


data class ServiceData(
    val id: Long,
    val name: String,
    val departmentId: Long,
    val categoryId: Long
)