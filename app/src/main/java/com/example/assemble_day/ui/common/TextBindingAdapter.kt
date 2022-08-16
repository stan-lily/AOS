package com.example.assemble_day.ui.common

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.assemble_day.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@BindingAdapter("isSaturday", "isSunday", "isSelectable")
fun updateDayColor(view: TextView, isSaturday: Boolean, isSunday: Boolean, isSelectable: Boolean) {
    if (!isSelectable) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.grey01))
    } else if (isSaturday) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
    } else if (isSunday) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.red))
    } else {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.black))
    }
}

@BindingAdapter("updateStartDate")
fun updateStartDateText(view: TextView, date: LocalDate?) {
    date?.let {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        view.text = it.format(formatter)
    } ?: kotlin.run {
        view.text = ""
    }
}

@BindingAdapter("updateEndDate")
fun updateEndDateText(view: TextView, date: LocalDate?) {
    date?.let {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        view.text =
            view.resources.getString(R.string.assemble_day_period_end, it.format(formatter))
    } ?: kotlin.run {
        view.text = ""
    }
}

@BindingAdapter("isAssembleDay")
fun updateAssembleDayBackground(view: TextView, isAssembleDay: Boolean) {
    if (isAssembleDay) {
        view.background =
            ContextCompat.getDrawable(view.context, R.drawable.background_storke_red_r20)
    } else {
        view.background =
            ContextCompat.getDrawable(view.context, R.color.white)
    }
}
