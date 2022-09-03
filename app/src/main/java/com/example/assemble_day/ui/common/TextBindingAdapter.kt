package com.example.assemble_day.ui.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_HEX

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