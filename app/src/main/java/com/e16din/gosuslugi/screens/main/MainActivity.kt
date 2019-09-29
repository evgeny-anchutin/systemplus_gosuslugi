package com.e16din.gosuslugi.screens.main

import android.app.ProgressDialog
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.helpers.ItemClickSupport
import com.e16din.gosuslugi.helpers.LocalCoroutineScope
import com.e16din.gosuslugi.helpers.getAuthHeader
import com.e16din.gosuslugi.screens.main.createrequest.CreateRequestActivity
import com.e16din.gosuslugi.screens.main.createrequest.CreateRequestScreenState
import com.e16din.gosuslugi.server.ApiResult
import com.e16din.gosuslugi.server.Requests
import com.e16din.gosuslugi.server.awaitResult
import kotlinx.android.synthetic.main.screen_main.*
import kotlinx.coroutines.launch


data class MainScreenState(
    var services: MutableList<ServiceItem> = ArrayList(),
    var searchQuery: String = ""
)

//todo: Сделать скрывающийся при скролле заголовок
class MainActivity : AppCompatActivity(), LocalCoroutineScope {

    class UserAgent
    class SystemAgent
    class ServerAgent

    // Subjects:

    val userAgent = UserAgent()
    val systemAgent = SystemAgent()
    val serverAgent = ServerAgent()

    // Fruits:

    val app get() = App.get
    val activity = this
    val screenState get() = app.screenStatesMap[MainActivity::class] as MainScreenState

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runActions()
        runSearchActions()
    }

    private fun runActions() = launch {
        userAgent.showMainLayout()
        systemAgent.initViewActions()
    }

    private fun runSearchActions() = launch {
        screenState.searchQuery = vSearchField.text.toString()

        userAgent.showServicesLoadingProgress()
        serverAgent.loadServices(query = screenState.searchQuery)
        userAgent.hideServicesLoadingProgress()
        userAgent.showServicesList()

        if (screenState.searchQuery.isNotBlank()) {
            userAgent.hideSearchButton()
            userAgent.showClearButton()
        }
    }

    fun UserAgent.showServicesLoadingProgress() = launch {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage("Загрузка услуг..")
        progressDialog?.show()
    }

    fun UserAgent.hideServicesLoadingProgress() {
        progressDialog?.hide()
    }

    fun UserAgent.showMainLayout() {
        setContentView(R.layout.screen_main)

        vServicesList.layoutManager = LinearLayoutManager(activity)
        vServicesList.adapter = ServicesAdapter(screenState)
    }

    fun UserAgent.showServicesList() {
        vServicesList.adapter?.notifyDataSetChanged()
    }

    fun UserAgent.showCreateRequestScreen(request: ServiceItem) {
        app.screenStatesMap[CreateRequestActivity::class] = CreateRequestScreenState(request)
        val intent = Intent(activity, CreateRequestActivity::class.java)
        startActivity(intent)
    }

    fun SystemAgent.initViewActions() {
        ItemClickSupport.addTo(vServicesList).setOnItemClickListener { vList, position, v ->
            userAgent.showCreateRequestScreen(screenState.services[position])
        }

        vSearchButton.setOnClickListener {
            runSearchActions()
        }

        vClearButton.setOnClickListener {
            screenState.searchQuery = ""
            userAgent.showSearchFieldWithQuery()

            userAgent.showSearchButton()
            userAgent.hideClearButton()

            runSearchActions()
        }

        vSearchField.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                runSearchActions()
                handled = true
            }
            handled
        }
    }

    fun UserAgent.showSearchFieldWithQuery() {
        vSearchField.setText(screenState.searchQuery)
    }

    fun UserAgent.showSearchButton() {
        vSearchButton.visibility = View.VISIBLE
    }

    fun UserAgent.hideSearchButton() {
        vSearchButton.visibility = View.GONE
    }

    fun UserAgent.showClearButton() {
        vClearButton.visibility = View.VISIBLE
    }

    fun UserAgent.hideClearButton() {
        vClearButton.visibility = View.GONE
    }

    suspend fun ServerAgent.loadServices(query: String) {
        val result = Requests.commonApi.loadServices(
            getAuthHeader(),
            query
        ).awaitResult()

        when (result) {
            is ApiResult.Success -> {
                val data = result.data!!
                val services = data.ServiceList!!
                    .map { service ->
                        val departments = data.DepartmentList!!
                        val department = departments.first { department ->
                            department.id == service.departmentId
                        }
                        ServiceItem(service.name, department.name)
                    }
                    .toMutableList()

                screenState.services = services
            }
            is ApiResult.Error -> {
                Log.e("debug", "loadServices.error")
                //todo: handle errors
            }
            is ApiResult.Fail -> {
                Log.e("debug", "loadServices.fail")
                //todo: handle fails
            }
        }
    }
}
