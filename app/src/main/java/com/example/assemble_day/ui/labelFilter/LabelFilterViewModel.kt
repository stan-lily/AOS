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
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class LabelFilterViewModel @Inject constructor(private val labelFilterRepository: LabelRepository) :
    ViewModel() {

    private var _selectedLabel: Label? = null
    val selectedLabel: Label? get() = _selectedLabel

    private val initialLabelList = mutableListOf<Label>()
    private val _labelStateFlow = MutableStateFlow(listOf<Label>())
    val labelStateFlow = _labelStateFlow.asStateFlow()

    private var _isSelectingFlagStateFlow = MutableStateFlow(false)
    val isSelectingFlagStateFlow = _isSelectingFlagStateFlow.asStateFlow()

    private var previousSelectedPosition = -1

    init {
        getLabel()
    }

    private fun getLabel() {
        viewModelScope.launch {
            val networkResult = labelFilterRepository.getLabel()
            when (networkResult) {
                is NetworkResult.Success -> {
                    println("다시조회")
                    val loadedLabelList = networkResult.data.toLabel()
                    initialLabelList.clear()
                    initialLabelList.addAll(loadedLabelList)
                    _labelStateFlow.value = loadedLabelList
                }
            }
        }
    }

    /* private fun getLabel() {
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
     }*/

    fun initPreviousSelectedPosition() {
        previousSelectedPosition = -1
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
        _isSelectingFlagStateFlow.value = false
        labelList[selectedPosition] = labelList[selectedPosition].copy(isSelected = false)
        initPreviousSelectedPosition()
        _selectedLabel = null
    }

    private fun selectOtherLabel(selectedPosition: Int, labelList: MutableList<Label>) {
        _isSelectingFlagStateFlow.value = true
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
        getLabel()
    }
}