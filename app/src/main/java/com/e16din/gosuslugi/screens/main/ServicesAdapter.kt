package com.e16din.gosuslugi.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e16din.gosuslugi.R
import kotlinx.android.synthetic.main.item_request_history.view.*

data class ServiceItem(
    var name: String = "Получить загранпаспорт",
    var departmentName: String = "УФМС России по Республике Татарстан"
)

class ServicesAdapter(var screenState: MainScreenState) :
    RecyclerView.Adapter<ServicesAdapter.ServiceVh>() {

    class UserAgent

    // Subjects:

    val userAgent = UserAgent()

    // Fruits:


    override fun getItemCount() = screenState.serviceItemList.size

    override fun onCreateViewHolder(vParent: ViewGroup, viewType: Int): ServiceVh {
        val view = LayoutInflater.from(vParent.context)
            .inflate(R.layout.item_service, vParent, false)

        return ServiceVh(view)
    }

    override fun onBindViewHolder(holder: ServiceVh, position: Int) {
        val item = screenState.serviceItemList[position]

        userAgent.showRequestName(holder.itemView, item.name)
        userAgent.showDepartmentName(holder.itemView, item.departmentName)
    }

    fun UserAgent.showRequestName(view: View, requestName: String) {
        view.vRequestNameLabel.text = requestName
    }

    fun UserAgent.showDepartmentName(view: View, departmentName: String) {
        view.vDateLabel.text = departmentName
    }

    class ServiceVh(view: View) : RecyclerView.ViewHolder(view)
}
