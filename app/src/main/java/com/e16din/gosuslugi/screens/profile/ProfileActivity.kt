package com.e16din.gosuslugi.screens.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.helpers.ItemClickSupport
import com.e16din.gosuslugi.helpers.LocalCoroutineScope
import kotlinx.android.synthetic.main.screen_profile.*

class ProfileActivity : AppCompatActivity(), LocalCoroutineScope {

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
    val screenState get() = app.screenStatesMap[ProfileActivity::class] as Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runActions()
    }

    private fun runActions() {
        userAgent.showProfileLayout()
        systemAgent.initActions()
        userAgent.showRequestsHistoryList()

    }

    fun UserAgent.showRequestDetailsScreen() {
        val intent = Intent(activity, RequestDetailsActivity::class.java)
        startActivity(intent)
    }

    fun UserAgent.showProfileLayout() {
        setContentView(R.layout.screen_profile)
    }

    fun UserAgent.showRequestsHistoryList() {
        vRequestsHistoryList.layoutManager = LinearLayoutManager(activity)

        val items = ArrayList<RequestHistoryItem>()
        items.add(RequestHistoryItem())
        items.add(RequestHistoryItem())
        items.add(RequestHistoryItem())
        vRequestsHistoryList.adapter = RequestsHistoryAdapter(items)
    }

    fun SystemAgent.initActions() {
        ItemClickSupport.addTo(vRequestsHistoryList).setOnItemClickListener { vList, position, v ->
            userAgent.showRequestDetailsScreen()
        }
    }
}
