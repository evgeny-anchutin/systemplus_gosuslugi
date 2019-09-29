package com.e16din.gosuslugi.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.helpers.LocalCoroutineScope
import com.e16din.gosuslugi.screens.main.MainActivity
import com.e16din.gosuslugi.screens.main.MainScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity(), LocalCoroutineScope {

    class UserAgent
    class SystemAgent
    class ServerAgent
    class ScreenAgent
    class AppAgent

    // Subjects:

    val userAgent = UserAgent()
    val systemAgent = SystemAgent()
    val serverAgent = ServerAgent()
    val screenAgent = ScreenAgent()
    val appAgent = AppAgent()

    // Fruits:

    val app get() = App.get
    val activity = this
    val screenState get() = app.screenStatesMap[SplashActivity::class]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runActions()
    }

    private fun runActions() = launch {
        userAgent.showSplashLayout()
        systemAgent.delayMs(3 * 1000)
        //todo: сделать индикатор загрузки, можно кружками по одному
        userAgent.showMainScreen()
    }

    fun UserAgent.showMainScreen() {
        app.screenStatesMap[MainActivity::class] =
            MainScreenState() //todo: добавить кеширование сервисов
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    fun UserAgent.showSplashLayout() {
        setContentView(R.layout.screen_splash)
    }

    suspend fun SystemAgent.delayMs(delayMs: Long) = delay(delayMs)
}
