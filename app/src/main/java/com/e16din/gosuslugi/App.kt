package com.e16din.gosuslugi

import androidx.multidex.MultiDexApplication
import kotlin.reflect.KClass


class App : MultiDexApplication() {

    companion object {
        lateinit var get: App
    }

    init {
        get = this
    }

    var screenStatesMap = HashMap<KClass<*>, Any?>()

    override fun onCreate() {
        super.onCreate()
    }
}