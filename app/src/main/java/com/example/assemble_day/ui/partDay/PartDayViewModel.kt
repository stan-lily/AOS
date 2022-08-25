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
                    label = Label("test", "test", "#FFFF3B30", "#000000"),
                    title = "test 입니다"
                )
            )
        }
    }

}