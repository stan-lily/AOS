package com.example.assemble_day.ui.labelFilter

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.Label
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LabelFilterViewModel : ViewModel() {

    private var _selectedLabel: Label? = null
    val selectedLabel: Label? get() = _selectedLabel

    private val initialLabelList = mutableListOf<Label>()
    private val _labelStateFlow = MutableStateFlow(listOf<Label>())
    val labelStateFlow = _labelStateFlow.asStateFlow()

    private var previousSelectedPosition = -1

    init {
        createDummyLabelList()
    }

    private fun createDummyLabelList() {
        val labelList = mutableListOf<Label>()
        labelList.add(Label(1, "BE", "dev-team", "#1e90ff", "BRIGHT"))
        labelList.add(Label(2, "AOS", "dev-team", "#ff00ff", "BRIGHT"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))
        labelList.add(Label(3, "docs", "docs", "#adff2f", "DARK"))

        initialLabelList.addAll(labelList)
        _labelStateFlow.value = labelList
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
}