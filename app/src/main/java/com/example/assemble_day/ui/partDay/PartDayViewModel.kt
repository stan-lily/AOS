package com.example.assemble_day.ui.partDay

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.model.PartDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PartDayViewModel @Inject constructor() : ViewModel() {

    val loadedPartDayList = mutableListOf<PartDay>()
    private var selectedLabelForNewTarget: Label? = null
    private var newTargetTitle = ""

    private val initialTargetList = mutableListOf<PartDayTarget>()
    private val _loadedTargetStateFlow = MutableStateFlow(listOf<PartDayTarget>())
    val loadedTargetStateFlow = _loadedTargetStateFlow.asStateFlow()

    private var _isFiltering = false
    val isFiltering get() = _isFiltering

    private var _isCreatingTarget = false
    val isCreatingTarget get() = _isCreatingTarget

    private var _inputTargetTitleStateFlow = MutableStateFlow("")
    val inputTargetTitleStateFlow = _inputTargetTitleStateFlow.asStateFlow()

    private val _createTargetFlagSharedFlow = MutableSharedFlow<Boolean>(replay = 1)
    val createTargetFlagSharedFlow = _createTargetFlagSharedFlow.asSharedFlow()

    private val _validateTargetFlagSharedFlow = MutableSharedFlow<Boolean>(replay = 1)
    val validateTargetFlagSharedFlow = _validateTargetFlagSharedFlow.asSharedFlow()

    private var selectedTargetLabelPosition = -1

    init {
        createDummyAssembleDay()
        createDummyTarget()
    }

    private fun createDummyAssembleDay() {
        val loadedAssembleDay =
            AssembleDay("Test 입니다", LocalDate.of(2022, 8, 19), LocalDate.of(2022, 8, 30))

        val size = loadedAssembleDay.end.compareTo(loadedAssembleDay.start)
        var count = 0L
        repeat(size) {
            loadedPartDayList.add(
                PartDay(
                    loadedAssembleDay.start.plusDays(count),
                    loadedAssembleDay,
                    listOf<PartDayTarget>()
                )
            )
            count++
        }
        println(loadedPartDayList)
    }

    private fun createDummyTarget() {
        initialTargetList.clear()
        val dummyTargetList = mutableListOf<PartDayTarget>()
        repeat(3) {
            dummyTargetList.add(
                PartDayTarget(
                    label = Label(
                        id = 1,
                        name = "BE",
                        description = "dev-team",
                        backgroundColor = "#1e90ff",
                        fontColor = "BRIGHT"
                    ),
                    title = "BE 입니다"
                )
            )
        }
        repeat(4) {
            dummyTargetList.add(
                PartDayTarget(
                    label = Label(2, "AOS", "dev-team", "#ff00ff", "BRIGHT"),
                    title = "AOS 입니다"
                )
            )
        }
        repeat(3) {
            dummyTargetList.add(
                PartDayTarget(
                    label = Label(3, "docs", "docs", "#adff2f", "DARK"),
                    title = "docs 입니다"
                )
            )
        }
        initialTargetList.addAll(dummyTargetList)
        _loadedTargetStateFlow.value = dummyTargetList
    }

    fun setFilteringFlag(isFiltering: Boolean) {
        _isFiltering = isFiltering
        _isCreatingTarget = false
    }

    fun filterLabel(selectedLabel: Label?) {
        selectedLabel?.let {
            val filteredTargetList = initialTargetList.filter { target ->
                target.label.name == selectedLabel.name
            }
            _loadedTargetStateFlow.value = filteredTargetList
        } ?: kotlin.run {
            _loadedTargetStateFlow.value = initialTargetList
        }
    }

    fun selectTargetLabel(selectedPosition: Int) {
        selectedTargetLabelPosition = selectedPosition
    }

    fun updateTargetLabel(selectedLabel: Label?) {
        selectedLabel?.let { label ->
            val updatedList = _loadedTargetStateFlow.value.toMutableList()
            updatedList[selectedTargetLabelPosition] =
                updatedList[selectedTargetLabelPosition].copy(label = label)
            _loadedTargetStateFlow.value = updatedList
        }
    }

    fun setTargetTitle(title: String) {
        if (validateTargetTitle(title)) {
            _inputTargetTitleStateFlow.value = title
            _isCreatingTarget = true
            _isFiltering = false
        }
    }

    fun createNewTarget(selectedLabel: Label?) {
        _createTargetFlagSharedFlow.tryEmit(selectedLabel != null)
        selectedLabel?.let {
            val newTarget =
                PartDayTarget(title = _inputTargetTitleStateFlow.value, label = selectedLabel)
            initialTargetList.add(0, newTarget)
            _loadedTargetStateFlow.value = initialTargetList.toList()
        }
    }

    private fun validateTargetTitle(title: String): Boolean {
        _validateTargetFlagSharedFlow.tryEmit(title.isNotBlank() && title.isNotEmpty())
        return title.isNotEmpty() && title.isNotBlank()
    }

    fun selectTarget(selectedPosition: Int) {
        val targetList = _loadedTargetStateFlow.value.toMutableList()
        targetList[selectedPosition] = targetList[selectedPosition].copy(isSelected = true)
        _loadedTargetStateFlow.value = targetList
    }

    fun updateTarget(updatedTitle: String, selectedPosition: Int) {
        val targetList = _loadedTargetStateFlow.value.toMutableList()
        targetList[selectedPosition] = targetList[selectedPosition].copy(isSelected = false)
        if (validateTargetTitle(updatedTitle)) {
            val currentTargetTitle = _loadedTargetStateFlow.value[selectedPosition].title
            if (currentTargetTitle != updatedTitle) {
                targetList[selectedPosition] =
                    targetList[selectedPosition].copy(title = updatedTitle)
            }
        }
        _loadedTargetStateFlow.value = targetList
    }

}