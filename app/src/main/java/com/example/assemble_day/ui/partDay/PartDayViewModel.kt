package com.example.assemble_day.ui.partDay

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.model.PartDay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class PartDayViewModel : ViewModel() {

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

    private var selectedTargetPosition = -1

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

    fun selectTarget(selectedPosition: Int) {
        selectedTargetPosition = selectedPosition
    }

    fun updateTargetLabel(selectedLabel: Label?) {
        selectedLabel?.let { label ->
            val updatedList = _loadedTargetStateFlow.value.toMutableList()
            updatedList[selectedTargetPosition] =
                updatedList[selectedTargetPosition].copy(label = label)
            _loadedTargetStateFlow.value = updatedList
        }
    }

    fun setTargetTitle(title: String) {
        _inputTargetTitleStateFlow.value = title
        _isCreatingTarget = true
        _isFiltering = false
    }

    fun createNewTarget(selectedLabel: Label?) {
        _createTargetFlagSharedFlow.tryEmit(selectedLabel != null)
        // TODO 새로운 타켓 생성
        selectedLabel?.let {
            val newTarget =
                PartDayTarget(title = _inputTargetTitleStateFlow.value, label = selectedLabel)
            initialTargetList.add(0, newTarget)
            _loadedTargetStateFlow.value = initialTargetList.toList()
        }
    }

}