package com.example.assemble_day.ui.labelFilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.toLabel
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.repository.LabelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelFilterViewModel @Inject constructor(private val labelFilterRepository: LabelRepository) : ViewModel() {

    private var _selectedLabel: Label? = null
    val selectedLabel: Label? get() = _selectedLabel

    private val initialLabelList = mutableListOf<Label>()
    private val _labelStateFlow = MutableStateFlow(listOf<Label>())
    val labelStateFlow = _labelStateFlow.asStateFlow()

    private var previousSelectedPosition = -1

    init {
        viewModelScope.launch {
            getLabel()
        }
    }

    private suspend fun getLabel() {
        val networkResult = labelFilterRepository.getLabel()
        when (networkResult) {
            is NetworkResult.Success -> {
                val loadedLabelList = networkResult.data.toLabel()
                initialLabelList.addAll(loadedLabelList)
                _labelStateFlow.value = loadedLabelList
            }
        }
    }

    fun selectLabel(selectedPosition: Int) {
        val labelList = _labelStateFlow.value.toMutableList()
        val doesSelectSameLabel = labelList[selectedPosition].isSelected

        if (previousSelectedPosition >= 0) {
            labelList[previousSelectedPosition] =
                labelList[previousSelectedPosition].copy(isSelected = false)
        }

        if (doesSelectSameLabel) {
            selectSameLabel(selectedPosition, labelList)
        } else {
            selectOtherLabel(selectedPosition, labelList)
        }

        _labelStateFlow.value = labelList
    }

    private fun selectSameLabel(selectedPosition: Int, labelList: MutableList<Label>) {
        labelList[selectedPosition] = labelList[selectedPosition].copy(isSelected = false)
        previousSelectedPosition = -1
        _selectedLabel = null
    }

    private fun selectOtherLabel(selectedPosition: Int, labelList: MutableList<Label>) {
        labelList[selectedPosition] = labelList[selectedPosition].copy(isSelected = true)
        previousSelectedPosition = selectedPosition
        _selectedLabel = labelList[selectedPosition]
    }

    fun initSelectedLabel() {
        _selectedLabel = null
    }

    fun searchLabel(inputText: String) {
        val labelList = initialLabelList
        val filteredLabelList = mutableListOf<Label>()

        if (inputText.isNotEmpty() || inputText.isNotBlank()) {
            filteredLabelList.addAll(labelList.filter { label ->
                label.name.contains(inputText.lowercase()) || label.name.contains(inputText.uppercase())
            })
        } else {
            filteredLabelList.addAll(initialLabelList)
        }

        _labelStateFlow.value = filteredLabelList
    }

    fun updateLabel() {
        viewModelScope.launch {
            getLabel()
        }
    }

}