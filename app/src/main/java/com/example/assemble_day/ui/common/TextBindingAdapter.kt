package com.example.assemble_day.ui.common

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("updateMonth")
fun updateMonthText(view: TextView, month: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
    view.text = month.format(formatter)
}