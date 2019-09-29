package com.e16din.gosuslugi.server.data.services.fields


data class ServiceField(
    val id: Long,
    val name: String, // "Произвольное поле"
    val category: String, // "ФизическоеЛицо"
    val type: ArrayList<String> // ["Значения полей заявления", "Строка"]
)