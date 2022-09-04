package com.example.assemble_day.ui.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX

@BindingAdapter("targetLabelBtnBackgroundTint")
fun updateBackgroundTint(view: View, color: String?) {
    color?.let {
        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
    } ?: kotlin.run {
        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(LABEL_FONT_COLOR_BLACK_HEX))
    }

}