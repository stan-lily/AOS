package com.example.assemble_day.ui.labelCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_STRING
import com.example.assemble_day.common.Constants.NULL_VALUE
import com.example.assemble_day.data.remote.dto.Labels
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.repository.LabelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelCreateViewModel @Inject constructor(private val labelRepository: LabelRepository) : ViewModel() {

    private var nameFlag = false
    private var name = ""
    private var description = ""
    private var backgroundColorFlag = false

    private val _fontColorStateFlow = MutableStateFlow(LABEL_FONT_COLOR_BLACK_STRING)
    val fontColorStateFlow = _fontColorStateFlow.asStateFlow()

    private val _backgroundColorStateFlow = MutableStateFlow("")
    val backgroundColorStateFlow = _backgroundColorStateFlow.asStateFlow()

    private val _saveActionStateFlow = MutableStateFlow(false)
    val saveActionStateFlow = _saveActionStateFlow.asStateFlow()

    fun setTitle(inputText: String) {
        nameFlag = inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank() && !inputText.contains(" ")
        if (nameFlag) name = inputText
        validateActionFlag()
    }

    fun setDescription(inputText: String) {
        description = inputText
    }

    fun setBackgroundColor(inputText: String) {
        backgroundColorFlag =
            inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank()
        if (backgroundColorFlag) _backgroundColorStateFlow.value = inputText
        validateActionFlag()
    }

    private fun validateActionFlag() {
        _saveActionStateFlow.value = nameFlag && backgroundColorFlag
    }

    fun createRandomColor() {
        val red = (0..255).random().toString(16)
        val green = (0..255).random().toString(16)
        val blue = (0..255).random().toString(16)
        _backgroundColorStateFlow.value = "#$red$green$blue"
    }

    fun setFontColor(color: String) {
        _fontColorStateFlow.value = color
    }

    fun saveLabel() {
        val newLabel = Label(
            name = name,
            description = description,
            backgroundColor = _backgroundColorStateFlow.value.uppercase(),
            fontColor = _fontColorStateFlow.value
        )
        viewModelScope.launch {
            labelRepository.createLabel(newLabel)
        }
    }
}