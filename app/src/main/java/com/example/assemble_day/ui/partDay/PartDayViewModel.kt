package com.example.assemble_day.ui.partDay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assemble_day.data.remote.NetworkResult
import com.example.assemble_day.data.remote.dto.toPartDayList
import com.example.assemble_day.data.remote.dto.toPartDayTarget
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDay
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.repository.TargetRepository
import com.example.assemble_day.ui.common.toStringFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PartDayViewModel @Inject constructor(private val targetRepository: TargetRepository) :
    ViewModel() {

    private var currentPartDayDate = ""
    private var newTargetTitle = ""
    private var assembleId = 1

//    private val _loadedTargetCountsListStateFlow = MutableStateFlow(listOf<PartDay>())
//    val loadedTargetCountsListStateFlow = _loadedTargetCountsListStateFlow.asStateFlow()

    private val _loadedPartDayListStateFlow = MutableStateFlow(listOf<PartDay>())
    val loadedPartDayListStateFlow = _loadedPartDayListStateFlow.asStateFlow()

    private var _isInitPartDayList = true
    val isInitPartDayList get() = _isInitPartDayList

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
        viewModelScope.launch {
            getPartDays()
        }
//        createDummyAssembleDay()
    }

    /*private fun createDummyAssembleDay() {
        val loadedAssembleDay =
            AssembleDay("Test 입니다", LocalDate.of(2022, 8, 19), LocalDate.of(2022, 8, 30))
        val partDayList = mutableListOf<PartDay>()
        val size = loadedAssembleDay.assembleEndAt.compareTo(loadedAssembleDay.assembleStartAt)
        var count = 0L
        repeat(size) {
            val date = loadedAssembleDay.assembleStartAt.plusDays(count)
            partDayList.add(
                PartDay(
                    date,
                    loadedAssembleDay,
                    createDummyTarget(date.dayOfMonth)
                )
            )
            count++
        }
        _loadedPartDayListStateFlow.value = partDayList
    }*/

    /*private fun createDummyTarget(day: Int): List<PartDayTarget> {
        initialTargetList.clear()
        val dummyTargetList = mutableListOf<PartDayTarget>()
        val firstLabel = Label(1, "BE", "dev-team", "#1e90ff", "BRIGHT")
        val secondLabel = Label(2, "AOS", "dev-team", "#ff00ff", "BRIGHT")
        val thirdLabel = Label(3, "docs", "docs", "#adff2f", "DARK")

        dummyTargetList.add(PartDayTarget(firstLabel, "$day BE 입니다"))
        dummyTargetList.add(PartDayTarget(firstLabel, "$day 테스트입니다"))
        dummyTargetList.add(PartDayTarget(firstLabel, "$day 대단합니다"))
        dummyTargetList.add(PartDayTarget(firstLabel, "$day 힘듭니다"))
        dummyTargetList.add(PartDayTarget(secondLabel, "$day 좋습니다"))
        dummyTargetList.add(PartDayTarget(secondLabel, "$day 배고픕니다"))
        dummyTargetList.add(PartDayTarget(secondLabel, "$day 점심"))
        dummyTargetList.add(PartDayTarget(secondLabel, "$day 힘들어"))
        dummyTargetList.add(PartDayTarget(secondLabel, "$day 테스트"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day 잘되기를"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day BE"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day AOS"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day 취직"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day 안녕"))
        dummyTargetList.add(PartDayTarget(thirdLabel, "$day 잘가"))

        return dummyTargetList
    }*/

    private suspend fun getPartDays() {
        val targetCounts = targetRepository.getTargetCounts(assembleId)
        when (targetCounts) {
            is NetworkResult.Success -> {
                val partDayList = targetCounts.data.toPartDayList()
                _loadedPartDayListStateFlow.value = partDayList
            }
        }
    }

/*    private suspend fun getTargets() {
        val today = LocalDate.now()
        val targetList = targetRepository.getTargets(today)
        when (targetList) {
            is NetworkResult.Success -> {
                _loadedTargetStateFlow.value = targetList.data.targets
            }
        }
    }*/

    fun selectPartDay(selectedPartDayDate: String) {
        viewModelScope.launch {
            currentPartDayDate = selectedPartDayDate
            val targetList = targetRepository.getTargets(currentPartDayDate)
            when (targetList) {
                is NetworkResult.Success -> {
                    val partDayTargetList = targetList.data.toPartDayTarget()
                    initialTargetList.clear()
                    initialTargetList.addAll(partDayTargetList)
                    _loadedTargetStateFlow.value = partDayTargetList
                }
            }
        }
    }

    fun setInitPartDayListFlag() {
        _isInitPartDayList = false
    }

    fun setFilteringFlag(isFiltering: Boolean) {
        _isFiltering = isFiltering
        _isCreatingTarget = false
    }

    fun filterLabel(selectedLabel: Label?) {
        selectedLabel?.let {
            val filteredTargetList = initialTargetList.filter { target ->
                target.labelId == selectedLabel.id
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
                updatedList[selectedTargetLabelPosition].copy(labelId = label.id)
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

    /* fun createNewTarget(selectedLabel: Label?) {
     _createTargetFlagSharedFlow.tryEmit(selectedLabel != null)
     selectedLabel?.let {
         val newTarget =
             PartDayTarget(title = _inputTargetTitleStateFlow.value, label = selectedLabel, labelId = selectedLabel.id, targetAt = "2022-7-02")
         initialTargetList.add(0, newTarget)
         _loadedTargetStateFlow.value = initialTargetList.toList()
     }
 }*/

    fun createNewTarget(selectedLabel: Label?) {
        _createTargetFlagSharedFlow.tryEmit(selectedLabel != null)
        selectedLabel?.let { label ->
            viewModelScope.launch {
                val newTarget =
                    PartDayTarget(
                        title = _inputTargetTitleStateFlow.value,
                        labelId = label.id,
                        targetAt = currentPartDayDate
                    )
                val requestCreateTarget = targetRepository.createTarget(assembleId, newTarget)
                when (requestCreateTarget) {
                    is NetworkResult.Success -> {
                        selectPartDay(currentPartDayDate)
                    }
                }
            }
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
        // TODO target 업데이트 put 요청
    }

    fun deleteTarget(selectedPosition: Int) {
        val targetList = _loadedTargetStateFlow.value.toMutableList()
        targetList.removeAt(selectedPosition)
        _loadedTargetStateFlow.value = targetList
    }

    fun moveTargetToOtherPartDay(
        droppedTarget: PartDayTarget,
        droppedTargetPosition: Int,
        itemPosition: Int
    ) {
//        if (!doesDropTargetToSamePartDay(droppedTarget, itemPosition)) {
//            deleteTarget(droppedTargetPosition)
//            val initialPartDayList = _loadedPartDayListStateFlow.value.toMutableList()
//            val itemTargetList =
//                _loadedPartDayListStateFlow.value[itemPosition].targetList.toMutableList()
//            itemTargetList.add(droppedTarget)
//            initialPartDayList[itemPosition] =
//                _loadedPartDayListStateFlow.value[itemPosition].copy(targetList = itemTargetList)
//            _loadedPartDayListStateFlow.value = initialPartDayList
//        }
        // TODO Target 이동 Put 요청
    }

    private fun doesDropTargetToSamePartDay(
        droppedTarget: PartDayTarget,
        itemPosition: Int
    ): Boolean {
//        return _loadedPartDayListStateFlow.value[itemPosition].targetList.contains(droppedTarget)
        return _loadedTargetStateFlow.value.contains(droppedTarget)
    }
}

