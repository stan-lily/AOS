package com.example.assemble_day.ui.common.eventListener

import com.example.assemble_day.domain.model.Issue

interface IssueEventListener {
    fun switchToIssueEditMode()
    fun checkIssue(issue: Issue)
    fun uncheckIssue(issue: Issue)
}