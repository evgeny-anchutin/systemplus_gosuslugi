package com.e16din.gosuslugi.screens.main.createrequest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.server.data.services.fields.FieldValue
import kotlinx.android.synthetic.main.item_request_history.view.*


enum class FieldTypes {
    Values,
    Date,
    Number,
    Boolean,
    String
}

data class ServiceFieldItem(
    var fieldName: String,
    var types: List<String>,
    var values: List<FieldValue>
)

class ServiceFieldsAdapter(var items: ArrayList<ServiceFieldItem>) :
    RecyclerView.Adapter<ServiceFieldsAdapter.ServiceFieldVh>() {

    class UserAgent

    // Subjects:

    val userAgent = UserAgent()

    // Fruits:


    override fun getItemCount() = items.size

    override fun onCreateViewHolder(vParent: ViewGroup, viewType: Int): ServiceFieldVh {
        val layoutId = when (viewType) {
            FieldTypes.Values.ordinal -> R.layout.item_field_values // RadioGroup
            FieldTypes.Date.ordinal -> R.layout.item_field_date // DatePicker
            FieldTypes.Number.ordinal -> R.layout.item_field_number // EditText | Number
            FieldTypes.Boolean.ordinal -> R.layout.item_field_boolean // CheckBox
            FieldTypes.String.ordinal -> R.layout.item_field_string // EditText | String
            else -> R.layout.item_field_unknown // TextView | Неизвестный тип поля: fieldName
        }

        val view = LayoutInflater.from(vParent.context)
            .inflate(layoutId, vParent, false)

        return ServiceFieldVh(view)
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]

        // NOTE: Важен порядок обработки условий (из-за полей с двумя типами)
        when {
            item.types.contains("Значения полей заявления") ->
                return FieldTypes.Values.ordinal

            item.types.contains("Дата") ->
                return FieldTypes.Date.ordinal

            item.types.contains("Число") ->
                return FieldTypes.Number.ordinal

            item.types.contains("Булево") ->
                return FieldTypes.Boolean.ordinal

            item.types.contains("Строка") ->
                return FieldTypes.String.ordinal
        }

        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ServiceFieldVh, position: Int) {
        val item = items[position]

        val viewType = getItemViewType(position)
        when (viewType) {
            FieldTypes.Values.ordinal ->
                userAgent.showValuesField(holder.itemView, item)

            FieldTypes.Date.ordinal ->
                userAgent.showDateField(holder.itemView, item)

            FieldTypes.Number.ordinal ->
                userAgent.showNumberField(holder.itemView, item)

            FieldTypes.Boolean.ordinal ->
                userAgent.showBooleanField(holder.itemView, item)

            FieldTypes.String.ordinal ->
                userAgent.showStringField(holder.itemView, item)

            else ->
                userAgent.showUnknownField(holder.itemView, item)
        }
    }

    fun UserAgent.showNumberField(view: View, item: ServiceFieldItem) {

    }

    fun UserAgent.showUnknownField(view: View, item: ServiceFieldItem) {

    }

    fun UserAgent.showStringField(view: View, item: ServiceFieldItem) {

    }

    fun UserAgent.showBooleanField(view: View, item: ServiceFieldItem) {

    }

    fun UserAgent.showDateField(view: View, item: ServiceFieldItem) {

    }

    fun UserAgent.showValuesField(view: View, item: ServiceFieldItem) {
//        view..text = requestName
    }

    fun UserAgent.showRequestDate(view: View, date: String) {
        view.vDateLabel.text = date
    }

    class ServiceFieldVh(view: View) : RecyclerView.ViewHolder(view)
}
