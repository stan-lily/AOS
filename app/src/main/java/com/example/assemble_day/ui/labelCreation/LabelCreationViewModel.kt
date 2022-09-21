package com.example.assemble_day.ui.labelCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.NULL_VALUE
import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.repository.LabelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelCreationViewModel @Inject constructor(private val labelRepository: LabelRepository) :
    ViewModel() {

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

    private val _resetActionStateFlow = MutableStateFlow(false)
    val resetActionStateFlow = _resetActionStateFlow.asStateFlow()

    private val _networkCreateSuccessStateFlow = MutableStateFlow(false)
    val networkCreateSuccessStateFlow = _networkCreateSuccessStateFlow.asStateFlow()

    private val _networkUpdateSuccessStateFlow = MutableStateFlow(false)
    val networkUpdateSuccessStateFlow = _networkUpdateSuccessStateFlow.asStateFlow()

    private val _networkDeleteSuccessStateFlow = MutableStateFlow(false)
    val networkDeleteSuccessStateFlow = _networkDeleteSuccessStateFlow.asStateFlow()

    fun setTitle(inputText: String) {
        nameFlag =
            inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank()
        if (nameFlag) name = inputText
        validateActionFlag()
    }

    fun setDescription(inputText: String) {
        description = inputText
    }

    fun setBackgroundColor(isColorFormat: Boolean, inputText: String) {
        backgroundColorFlag =
            inputText.isNotEmpty() && inputText != NULL_VALUE && inputText.isNotBlank() && isColorFormat
        if (backgroundColorFlag) _backgroundColorStateFlow.value = inputText
        validateActionFlag()
    }

    private fun validateActionFlag() {
        _saveActionStateFlow.value = nameFlag && backgroundColorFlag
    }

    fun createRandomColor() {
        var red = (0..255).random().toString(16)
        var green = (0..255).random().toString(16)
        var blue = (0..255).random().toString(16)

        if (red.length < 2) red = "0$red"
        if (green.length < 2) green = "0$green"
        if (blue.length < 2) blue = "0$blue"

        _backgroundColorStateFlow.value = "#$red$green$blue"
    }

    fun setFontColor(color: String) {
        _fontColorStateFlow.value = color
    }

    fun runFirstActionItem(label: Label?) {
        label?.let {
            updateLabel(it)
        } ?: kotlin.run {
            saveLabel()
        }
    }

    private fun saveLabel() {
        val newLabel = Label(
            name = name,
            description = description,
            backgroundColor = _backgroundColorStateFlow.value.uppercase(),
            fontColor = _fontColorStateFlow.value
        )
         viewModelScope.launch {
            val createLabelRequest = labelRepository.createLabel(newLabel)
            when (createLabelRequest) {
                is NetworkResult.Success -> {
                    _networkCreateSuccessStateFlow.value = true
                }
            }
            _networkCreateSuccessStateFlow.value = false
        }
    }

    private fun updateLabel(label: Label) {
        val uppercaseLabel = label.copy(backgroundColor = label.backgroundColor.uppercase())
        val updatedLabel = Label(
            id = label.id,
            name = name,
            description = description,
            backgroundColor = _backgroundColorStateFlow.value.uppercase(),
            fontColor = _fontColorStateFlow.value,
            isSelected = label.isSelected
        )
        viewModelScope.launch {
            if (uppercaseLabel != updatedLabel) {
                val updateLabelRequest = labelRepository.updateLabel(updatedLabel.id, updatedLabel)
                when (updateLabelRequest) {
                    is NetworkResult.Success -> {
                        _networkUpdateSuccessStateFlow.value = true
                    }
                }
            }
            _networkUpdateSuccessStateFlow.value = false
        }
    }

    fun resetLabel() {
        _resetActionStateFlow.value = true
        nameFlag = false
        name = ""
        description = ""
        backgroundColorFlag = false
        _fontColorStateFlow.value = LABEL_FONT_COLOR_BLACK_STRING
        _backgroundColorStateFlow.value = ""
        _saveActionStateFlow.value = false
    }

    fun completedReset() {
        _resetActionStateFlow.value = false
    }

    fun deleteLabel(label: Label?) {
        label?.let { deletingLabel ->
            viewModelScope.launch {
                val deleteLabelRequest = labelRepository.deleteLabel(deletingLabel.id)
                when (deleteLabelRequest) {
                    is NetworkResult.Success -> {
                        _networkDeleteSuccessStateFlow.value = true
                    }
                    is NetworkResult.Error -> {
                        if (deleteLabelRequest.code == 204) {
                            _networkDeleteSuccessStateFlow.value = true
                        }

                    }
                    is NetworkResult.Exception -> {

                    }
                }
                _networkDeleteSuccessStateFlow.value = false
            }
        }
    }
}