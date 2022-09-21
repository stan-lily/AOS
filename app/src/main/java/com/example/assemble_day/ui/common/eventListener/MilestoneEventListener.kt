package com.example.assemble_day.ui.common.eventListener

import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.domain.model.Milestone

interface MilestoneEventListener {
    fun switchToIssueEditMode()
    fun checkMilestone(milestonePosition: Int)
    fun uncheckMilestone(milestonePosition: Int)
}