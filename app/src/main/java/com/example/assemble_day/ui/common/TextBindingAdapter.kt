package com.example.assemble_day.ui.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_HEX
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("labelBackgroundTint")
fun updateLabelBackgroundTint(view: View, color: String) {
    view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
}

@BindingAdapter("labelTextColor")
fun updateLabelTextColor(view: TextView, color: String) {
    when (color) {
        LABEL_FONT_COLOR_BLACK_STRING -> view.setTextColor(
            Color.parseColor(
                LABEL_FONT_COLOR_BLACK_HEX
            )
        )
        else -> view.setTextColor(Color.parseColor(LABEL_FONT_COLOR_WHITE_HEX))
    }
}

@BindingAdapter("isInputtingTitle")
fun updateTargetViewVisible(view: TextView, title: String) {
    if (title.isEmpty() || title.isBlank()) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0)
    } else {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add, 0)
    }
}

@BindingAdapter("backgroundColorText")
fun updateLabelCreatingViewBackgroundColorHexText(view: TextView, backgroundColor: String) {
    if (backgroundColor.isNotBlank() || backgroundColor.isNotEmpty()) {
        try {
            view.text = backgroundColor.uppercase()
        } catch (e: Throwable) {
            Log.d("Error", e.message, e)
        }
    }
}

@BindingAdapter("backgroundColor")
fun updateLabelCreatingViewBackgroundColor(view: TextView, backgroundColor: String) {
    if (backgroundColor.isNotBlank() || backgroundColor.isNotEmpty()) {
        val r = "${backgroundColor[1]}${backgroundColor[2]}".toInt(16)
        val g = "${backgroundColor[3]}${backgroundColor[4]}".toInt(16)
        val b = "${backgroundColor[5]}${backgroundColor[6]}".toInt(16)
        try {
            view.backgroundTintList = ColorStateList.valueOf(Color.rgb(r, g, b))
        } catch (e: Throwable) {
            Log.d("Error", e.message, e)
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.backgroundTintList =
                ColorStateList.valueOf(view.resources.getColor(R.color.grey03_alpha_10, null))
        } else {
            view.backgroundTintList =
                ColorStateList.valueOf(view.resources.getColor(R.color.grey03_alpha_10))
        }
    }
}

@BindingAdapter("fontColor")
fun updateLabelFontColor(view: TextView, fontColor: String) {
    if (fontColor == LABEL_FONT_COLOR_BLACK_STRING) {
        view.setTextColor(Color.parseColor(LABEL_FONT_COLOR_BLACK_HEX))
    } else {
        view.setTextColor(Color.parseColor(LABEL_FONT_COLOR_WHITE_HEX))
    }
}

@BindingAdapter("partDayDate")
fun updatePartDayDate(view: TextView, date: LocalDate?) {
    val formatter = DateTimeFormatter.ofPattern("dd")
    view.text = date?.format(formatter) ?: ""
}