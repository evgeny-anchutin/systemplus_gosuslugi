package com.e16din.gosuslugi.screens.main.createrequest

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.e16din.gosuslugi.App
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.helpers.LocalCoroutineScope
import com.e16din.gosuslugi.helpers.getAuthHeader
import com.e16din.gosuslugi.screens.main.ServiceItem
import com.e16din.gosuslugi.screens.profile.RequestDetailsActivity
import com.e16din.gosuslugi.server.ApiResult
import com.e16din.gosuslugi.server.Requests
import com.e16din.gosuslugi.server.awaitResult
import com.e16din.gosuslugi.server.data.services.ServiceData
import kotlinx.android.synthetic.main.screen_create_request.*
import kotlinx.coroutines.launch


data class CreateRequestScreenState(
    var serviceItem: ServiceItem,
    val serviceData: ServiceData,
    var serviceFieldItemList: MutableList<ServiceFieldItem> = ArrayList()
)

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

    // Temp:

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runActions()
    }

    private fun runActions() = launch {
        userAgent.showCreateRequestLayout()
        userAgent.showBackButton()
        systemAgent.initActions()

        userAgent.showDepartmentName()
        userAgent.showServiceName()

        userAgent.showServiceFieldsProgress()
        serverAgent.loadServiceFields()
        userAgent.hideServiceFieldsProgress()
        userAgent.showServiceFieldsList()
    }

    fun UserAgent.showDepartmentName() {
        vDepartmentNameLabel.text = screenState.serviceItem.departmentName
    }

    fun UserAgent.showServiceName() {
        vServiceNameLabel.text = screenState.serviceItem.name
    }

    fun UserAgent.showCreateRequestLayout() {
        setContentView(R.layout.screen_create_request)

        vServiceFieldsList.layoutManager = LinearLayoutManager(activity)
        vServiceFieldsList.adapter = ServiceFieldsAdapter(screenState)
    }

    fun UserAgent.showServiceFieldsList() {
        vServiceFieldsList.adapter?.notifyDataSetChanged()
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
        //todo:
    }

    suspend fun ServerAgent.loadServiceFields() {
        val result = Requests.commonApi.loadServiceFields(
            getAuthHeader(),
            screenState.serviceData.id
        ).awaitResult()


        when (result) {
            is ApiResult.Success -> {
                val data = result.data!!
                val fieldValues = data.FieldValues!!
                val serviceFields = data.ServiceFields!!

                val serviceFieldItemList = serviceFields.map { serviceFieldData ->

                    //                        val department = departments.first { department ->
//                            department.id == serviceData.departmentId
//                        }
                    ServiceFieldItem(
                        id = serviceFieldData.id,
                        fieldName = serviceFieldData.name,
                        types = serviceFieldData.type,
                        values = emptyList()
                    ) //todo:

                }.toMutableList()

                screenState.serviceFieldItemList = serviceFieldItemList
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

    fun UserAgent.showServiceFieldsProgress() {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage("Загрузка полей услуги..")
        progressDialog?.show()
    }

    fun UserAgent.hideServiceFieldsProgress() {
        progressDialog?.hide()
    }
}
