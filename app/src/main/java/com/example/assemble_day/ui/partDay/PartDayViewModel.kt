package com.example.assemble_day.ui.partDay

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.AssembleDay
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.PartDayTarget
import com.example.assemble_day.domain.model.PartDay
import java.time.LocalDate

class PartDayViewModel : ViewModel() {

    val loadedAssembleDayUnit = mutableListOf<PartDay>()
    val dummyTargets = mutableListOf<PartDayTarget>()
//    val dummyLabels = mutableListOf<Label>()

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
        repeat(10) {
            dummyTargets.add(
                PartDayTarget(
                    label = Label(name = "test", description = "test", backgroundColor = "#FFFF3B30", fontColor = "BRIGHT"),
                    title = "test 입니다"
                )
            )
        }
    }

    fun filterLabel(selectedLabel: Label?) {
        // TODO 선택 라벨이 null 이 아니면 필터링

        // TODO 선택 라벨이 null 이면 전체 리스트 다시 조회
    }

}