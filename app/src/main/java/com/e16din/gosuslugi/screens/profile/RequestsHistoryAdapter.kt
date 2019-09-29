package com.e16din.gosuslugi.screens.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e16din.gosuslugi.R
import kotlinx.android.synthetic.main.item_request_history.view.*

enum class RequestState { InWork, Success, Fail, None }

data class RequestHistoryItem(
    var requestName: String = "Получить загранпаспорт",
    var date: String = "Заявка от 10.09.2019",
    var state: RequestState = RequestState.None
)

class RequestsHistoryAdapter(var items: MutableList<RequestHistoryItem>) :
    RecyclerView.Adapter<RequestsHistoryAdapter.RequestHistoryVh>() {

    class UserAgent

    // Subjects:

    val userAgent = UserAgent()

    // Fruits:


    override fun getItemCount() = items.size

    override fun onCreateViewHolder(vParent: ViewGroup, viewType: Int): RequestHistoryVh {
        val view = LayoutInflater.from(vParent.context)
            .inflate(R.layout.item_request_history, vParent, false)

        return RequestHistoryVh(view)
    }

    override fun onBindViewHolder(holder: RequestHistoryVh, position: Int) {
        val item = items[position]

        userAgent.showRequestName(holder.itemView, item.requestName)
        userAgent.showRequestDate(holder.itemView, item.date)
        userAgent.showRequestState(holder.itemView, item.state)
    }

    fun UserAgent.showRequestName(view: View, requestName: String) {
        view.vRequestNameLabel.text = requestName
    }

    fun UserAgent.showRequestDate(view: View, date: String) {
        view.vDateLabel.text = date
    }

    fun UserAgent.showRequestState(view: View, state: RequestState) {
        view.vStateLabel.text = when (state) {
            RequestState.InWork -> "В обработке"
            RequestState.Success -> "Заявка одобрена"
            RequestState.Fail -> "Заявка отклонена"
            RequestState.None -> ""
        }
    }

    class RequestHistoryVh(view: View) : RecyclerView.ViewHolder(view)
}
