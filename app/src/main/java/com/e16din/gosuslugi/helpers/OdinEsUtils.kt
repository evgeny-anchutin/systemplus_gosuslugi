package com.e16din.gosuslugi.helpers

import android.util.Base64


fun getAuthHeader(): String {
    val name = "WebUser"
    val password = ""

    val authString = "$name:$password"

    val authEncBytes = Base64.encode(authString.toByteArray(), Base64.NO_WRAP)
    val authStringEnc = String(authEncBytes)
    val authHeader = "Basic $authStringEnc"
    return authHeader
}