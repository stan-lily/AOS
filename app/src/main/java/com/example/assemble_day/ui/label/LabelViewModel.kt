package com.example.assemble_day.ui.label

import androidx.lifecycle.ViewModel
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_HEX
import com.example.assemble_day.common.Constants.NULL_VALUE
import com.example.assemble_day.domain.model.Label
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LabelViewModel : ViewModel() {

    private var nameFlag = false
    private var name = ""
    private var description = ""
    private var backgroundColorFlag = false
    private var backgroundColor = ""
    private var fontColor = LABEL_FONT_COLOR_BLACK_HEX

    private val _saveActionStateFlow = MutableStateFlow(false)
    val saveActionStateFlow = _saveActionStateFlow.asStateFlow()

    fun setTitle(inputText: String) {
        nameFlag = inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank()
        if (nameFlag) name = inputText
        validateActionFlag()
    }

    fun setDescription(inputText: String) {
        description = inputText
    }

    fun setBackgroundColor(inputText: String) {
        backgroundColorFlag =
            inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank()
        if (backgroundColorFlag) backgroundColor = inputText
        validateActionFlag()
    }

    private fun validateActionFlag() {
        _saveActionStateFlow.value = nameFlag && backgroundColorFlag
    }

    fun createRandomColor(): Array<Int> {
        val red = (0..255).random()
        val green = (0..255).random()
        val blue = (0..255).random()

        return arrayOf(red, green, blue)
    }

    fun getFontColor() = fontColor

    fun setFontColor(color: String) {
        fontColor = when (color) {
            LABEL_FONT_COLOR_BLACK_STRING -> LABEL_FONT_COLOR_BLACK_HEX
            else -> LABEL_FONT_COLOR_WHITE_HEX
        }
    }

    fun saveLabel() {
        val newLabel = Label(
            name = name,
            description = description,
            backgroundColor = backgroundColor,
            fontColor = fontColor
        )
        println(newLabel)
    }
}