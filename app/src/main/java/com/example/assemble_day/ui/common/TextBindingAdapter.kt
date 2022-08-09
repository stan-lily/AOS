package com.example.assemble_day.ui.common

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.assemble_day.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("updateMonth")
fun updateMonthText(view: TextView, month: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
    view.text = month.format(formatter)
}

@BindingAdapter("isSaturday", "isSunday")
fun updateDayColor(view: TextView, isSaturday: Boolean, isSunday: Boolean) {
    if (isSaturday) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
    } else if (isSunday) {
        view.setTextColor(ContextCompat.getColor(view.context, R.color.red))
    }
}