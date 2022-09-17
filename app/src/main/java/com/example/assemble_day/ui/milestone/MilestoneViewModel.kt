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

    fun checkMilestone(milestone: Milestone) {
        checkingMilestoneList.add(milestone)
    }

    fun uncheckMilestone(milestone: Milestone) {
        checkingMilestoneList.remove(milestone)
    }
}