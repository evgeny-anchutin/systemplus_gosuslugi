package com.e16din.gosuslugi.screens.main.createrequest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.helpers.LocalCoroutineScope
import com.e16din.gosuslugi.helpers.getAuthHeader
import com.e16din.gosuslugi.screens.main.ServiceItem
import com.e16din.gosuslugi.screens.profile.RequestDetailsActivity
import com.e16din.gosuslugi.server.ApiResult
import com.e16din.gosuslugi.server.Requests
import com.e16din.gosuslugi.server.awaitResult
import kotlinx.android.synthetic.main.screen_create_request.*
import kotlinx.coroutines.launch


data class CreateRequestScreenState(var service: ServiceItem)

//todo: add back button
class CreateRequestActivity : AppCompatActivity(), LocalCoroutineScope {

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
    val screenState get() = app.screenStatesMap[CreateRequestActivity::class] as CreateRequestScreenState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runActions()
    }

    private fun runActions() = launch {
        userAgent.showCreateRequestLayout()
        userAgent.showBackButton()
        systemAgent.initActions()
        serverAgent.loadServiceFields()
    }

    fun UserAgent.showCreateRequestLayout() {
        setContentView(R.layout.screen_create_request)
    }

    fun UserAgent.showBackButton() {
        setSupportActionBar(vToolbarContainer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        vToolbarContainer.navigationIcon = getDrawable(R.drawable.ic_arrow_back_24dp)
    }

    fun UserAgent.showSuccessDialogScreen() {
        val intent = Intent(activity, RequestDetailsActivity::class.java)
        startActivity(intent)
    }

    fun UserAgent.showFailDialogScreen() {
        val intent = Intent(activity, RequestDetailsActivity::class.java)
        startActivity(intent)
    }

    fun UserAgent.hideScreen() {
        finish()
    }

    fun SystemAgent.initActions() {
        vCreateRequestButton.setOnClickListener {
            serverAgent.sendCreateRequest()
            onBackPressed()
        }

        vToolbarContainer.setNavigationOnClickListener {
            userAgent.hideScreen()
        }
    }

    fun ServerAgent.sendCreateRequest() {

    }

    suspend fun ServerAgent.loadServiceFields() {
        val result = Requests.commonApi.loadServiceFields(
            getAuthHeader(),
            12
//            screenState.service.id
        ).awaitResult()


        when (result) {
            is ApiResult.Success -> {
                val data = result.data!!

                //todo:
//                val services = data.ServiceFields
//                    .map { service ->
//                        val departments = data.DepartmentList!!
//                        val department = departments.first { department ->
//                            department.id == service.departmentId
//                        }
//                        ServiceItem(service.name, department.name)
//                    }
//                    .toMutableList()
//
//                screenState.services = services
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
