package com.example.assemble_day.ui.partDay

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.model.PartDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class PartDayViewModel : ViewModel() {

    val loadedAssembleDayUnit = mutableListOf<PartDay>()
    private var selectedLabel: Label? = null

    private val _loadedTargetStateFlow = MutableStateFlow(listOf<PartDayTarget>())
    val loadedTargetStateFlow = _loadedTargetStateFlow.asStateFlow()

    private var _isFiltering = false
    val isFiltering get() = _isFiltering

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
            loadedAssembleDayUnit.add(
                PartDay(
                    loadedAssembleDay.start.plusDays(count),
                    loadedAssembleDay,
                    listOf<PartDayTarget>()
                )
            )
            count++
        }
        println(loadedAssembleDayUnit)
    }

    private fun createDummyTarget() {
        val dummyTargetList = mutableListOf<PartDayTarget>()
        repeat(10) {
            dummyTargetList.add(
                PartDayTarget(
                    label = Label(name = "test", description = "test", backgroundColor = "#FFFF3B30", fontColor = "BRIGHT"),
                    title = "test 입니다"
                )
            )
        }
        _loadedTargetStateFlow.value = dummyTargetList
    }

    fun setFilteringFlag(isFiltering: Boolean) {
        _isFiltering = isFiltering
    }

    fun filterLabel(selectedLabel: Label?) {
        // TODO 선택 라벨이 null 이 아니면 필터링

        // TODO 선택 라벨이 null 이면 전체 리스트 다시 조회
    }

    fun selectTarget(selectedPosition: Int) {
        selectedTargetPosition = selectedPosition
    }

    fun updateTargetLabel(selectedLabel: Label?) {
        selectedLabel?.let { label ->
            val updatedList = _loadedTargetStateFlow.value.toMutableList()
            updatedList[selectedTargetPosition] = updatedList[selectedTargetPosition].copy(label = label)
            _loadedTargetStateFlow.value = updatedList
        }
    }

}