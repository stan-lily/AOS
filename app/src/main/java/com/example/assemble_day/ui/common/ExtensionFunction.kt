package com.example.assemble_day.ui.common

import android.view.MotionEvent
import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.setEndIconOnClickListener(clickEvent: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= right - compoundDrawables[2].bounds.width() + paddingStart + marginStart + paddingEnd + marginEnd) {
                    clickEvent.invoke()
                    return true

                }
            }
            performClick()
            return false
        }
    })
}