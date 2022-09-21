package com.example.assemble_day.ui.milestoneCreation

import androidx.lifecycle.ViewModel
import com.example.assemble_day.domain.model.Milestone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MilestoneCreationViewModel : ViewModel() {

    private var description = ""
    private var completedDate = ""

    private val _milestoneTitleStateFlow = MutableStateFlow("")
    val milestoneTitleStateFlow = _milestoneTitleStateFlow.asStateFlow()


    fun inputMilestoneTitle(inputText: String) {
        _milestoneTitleStateFlow.value = inputText
    }

    fun inputMilestoneDescription(inputText: String) {
        if (inputText.isNotEmpty() && inputText.isNotBlank()) {
            description = inputText
        }
    }

    fun inputCompletedDate(date: String) {
        completedDate = date
    }

    fun resetMilestone() {
        _milestoneTitleStateFlow.value = ""
        description = ""
        completedDate = ""
    }

    fun createMilestone() {
        val newMilestone = Milestone(
            title = _milestoneTitleStateFlow.value,
            description = description,
            date = completedDate,
            progress = 0
        )
        println(newMilestone)
    }

}