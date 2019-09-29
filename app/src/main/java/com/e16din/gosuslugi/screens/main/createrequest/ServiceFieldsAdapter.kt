package com.e16din.gosuslugi.screens.main.createrequest

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.e16din.gosuslugi.R
import com.e16din.gosuslugi.server.data.services.fields.FieldValueData
import kotlinx.android.synthetic.main.item_field_boolean.view.*
import kotlinx.android.synthetic.main.item_field_date.view.*
import kotlinx.android.synthetic.main.item_field_number.view.*
import kotlinx.android.synthetic.main.item_field_string.view.*
import kotlinx.android.synthetic.main.item_field_unknown.view.*
import kotlinx.android.synthetic.main.item_field_values.view.*


enum class FieldTypes {
    Values,
    Date,
    Number,
    Boolean,
    String
}

data class ServiceFieldItem(
    var id: Long,
    var fieldName: String,
    var types: List<String>,
    var values: List<FieldValueData>,

    var dateVal: ArrayList<Int> = arrayListOf(2019 - 18, 1, 1),
    var stringVal: String = "",
    var boolVal: Boolean = false,
    var numberVal: Int = 0,
    var valueVal: String = ""
)

class ServiceFieldsAdapter(var screenState: CreateRequestScreenState) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserAgent

    // Subjects:

    val userAgent = UserAgent()

    // Fruits:


    override fun getItemCount() = screenState.serviceFieldItemList.size

    override fun onCreateViewHolder(vParent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

        return when (viewType) {
            FieldTypes.Values.ordinal -> ServiceFieldValuesVh(view)
            FieldTypes.Date.ordinal -> ServiceFieldDateVh(view)
            FieldTypes.Number.ordinal -> ServiceFieldNumberVh(view)
            FieldTypes.Boolean.ordinal -> ServiceFieldBooleanVh(view)
            FieldTypes.String.ordinal -> ServiceFieldStringVh(view)
            else -> ServiceFieldUnknownVh(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = screenState.serviceFieldItemList[position]

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = screenState.serviceFieldItemList[position]

        val viewType = getItemViewType(position)
        when (viewType) {
            FieldTypes.Values.ordinal ->
                userAgent.showValuesField(holder.itemView, item, position)

            FieldTypes.Date.ordinal ->
                userAgent.showDateField(holder.itemView, item, position)

            FieldTypes.Number.ordinal ->
                userAgent.showNumberField(holder.itemView, item, position)

            FieldTypes.Boolean.ordinal ->
                userAgent.showBooleanField(holder.itemView, item, position)

            FieldTypes.String.ordinal ->
                userAgent.showStringField(holder.itemView, item, position)

            else ->
                userAgent.showUnknownField(holder.itemView, item, position)
        }
    }

    fun UserAgent.showNumberField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        view.vFieldNumberNameLabel.text = item.fieldName
    }

    fun UserAgent.showUnknownField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        view.vFieldUnknownNamLabel.text = "Неизвестный тип поля: ${item.fieldName}"
    }

    fun UserAgent.showStringField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        view.vFieldStringNameLabel.text = item.fieldName
    }

    fun UserAgent.showBooleanField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        view.vBooleanCheckbox.text = item.fieldName
    }

    fun UserAgent.showDateField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        view.vFieldDateNameLabel.text = item.fieldName
        view.vDateLabel.text = "${item.dateVal[2]}.${item.dateVal[1]}.${item.dateVal[0]}"

        view.vDateButton.setOnClickListener {
            DatePickerDialog(
                view.context,
                { view, year, monthOfYear, dayOfMonth ->
                    item.dateVal.clear()

                    item.dateVal.add(year)
                    item.dateVal.add(monthOfYear)
                    item.dateVal.add(dayOfMonth)

                    notifyItemChanged(position)
                },
                item.dateVal[0],
                item.dateVal[1],
                item.dateVal[2]
            ).show()
        }
    }

    fun UserAgent.showValuesField(
        view: View,
        item: ServiceFieldItem,
        position: Int
    ) {
        val radioButton = RadioButton(view.context)
        radioButton.text = "Stub"
        view.vValuesGroup.addView(radioButton)
//        view..text = requestName
    }

    class ServiceFieldValuesVh(view: View) : RecyclerView.ViewHolder(view)
    class ServiceFieldDateVh(view: View) : RecyclerView.ViewHolder(view)
    class ServiceFieldNumberVh(view: View) : RecyclerView.ViewHolder(view)
    class ServiceFieldBooleanVh(view: View) : RecyclerView.ViewHolder(view)
    class ServiceFieldStringVh(view: View) : RecyclerView.ViewHolder(view)
    class ServiceFieldUnknownVh(view: View) : RecyclerView.ViewHolder(view)
}
