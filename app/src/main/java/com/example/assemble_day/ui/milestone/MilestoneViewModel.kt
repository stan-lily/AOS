package com.example.assemble_day.ui.milestone

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.domain.model.Milestone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MilestoneViewModel : ViewModel() {

    private val initialMilestoneList = mutableListOf<Milestone>()
    private val _milestoneListStateFlow = MutableStateFlow(listOf<Milestone>())
    val milestoneListStateFlow = _milestoneListStateFlow.asStateFlow()

    private val _checkedMilestoneCountStateFlow = MutableStateFlow(0)
    val checkedMilestoneCountStateFlow = _checkedMilestoneCountStateFlow.asStateFlow()

    private val checkingMilestoneList = mutableListOf<Milestone>()

    init {
        createDummyMilestoneList()
    }

    private fun createDummyMilestoneList() {
        val milestoneList = mutableListOf<Milestone>()

        repeat(10) {
            milestoneList.add(
                Milestone(
                    id = it,
                    title = "테스트 $it",
                    description = "테스트입니다",
                    date = "2022-09-$it",
                    progress = 30,
                )
            )
        }
        initialMilestoneList.clear()
        initialMilestoneList.addAll(milestoneList)
        _milestoneListStateFlow.value = milestoneList
    }

    fun clearCheckingIssueList() {
        checkingMilestoneList.clear()
    }

    fun checkMilestone(milestonePosition: Int) {
        _checkedMilestoneCountStateFlow.value++
        val milestoneList = _milestoneListStateFlow.value.toMutableList()
        milestoneList[milestonePosition] = milestoneList[milestonePosition].copy(isChecked = true)
        _milestoneListStateFlow.value = milestoneList
        checkingMilestoneList.add(milestoneList[milestonePosition])
    }

    fun uncheckMilestone(milestonePosition: Int) {
        _checkedMilestoneCountStateFlow.value =
            if (_checkedMilestoneCountStateFlow.value < 1) 0 else _checkedMilestoneCountStateFlow.value - 1
        val milestoneList = _milestoneListStateFlow.value.toMutableList()
        milestoneList[milestonePosition] = milestoneList[milestonePosition].copy(isChecked = false)
        _milestoneListStateFlow.value = milestoneList
        checkingMilestoneList.remove(milestoneList[milestonePosition])
    }
}