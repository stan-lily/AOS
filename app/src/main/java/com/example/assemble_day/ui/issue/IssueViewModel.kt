package com.example.assemble_day.ui.issue

import androidx.lifecycle.ViewModel
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_STRING
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_STRING
import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.domain.model.Label
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IssueViewModel : ViewModel() {

    private val initialIssueList = mutableListOf<Issue>()
    private val _issueListStateFlow = MutableStateFlow(listOf<Issue>())
    val issueListStateFlow = _issueListStateFlow.asStateFlow()

    private val checkingIssueList = mutableListOf<Issue>()

    private var _isSearching = false
    val isSearching get() = _isSearching

    init {
        createDummyIssueList()
    }

    private fun createDummyIssueList() {
        val issueList = mutableListOf<Issue>()
        val dummyLabel = Label(
            name = "테스트",
            description = "테스트입니다",
            backgroundColor = LABEL_FONT_COLOR_BLACK_STRING,
            fontColor = LABEL_FONT_COLOR_WHITE_STRING
            )
        repeat(10) {
            issueList.add(Issue(
                id = it,
                title = "테스트 $it",
                description = "테스트입니다",
                label = dummyLabel,
                milestone = "마일스톤"
            ))
        }
        initialIssueList.clear()
        initialIssueList.addAll(issueList)
        _issueListStateFlow.value = issueList
    }

    fun clearCheckingIssueList() {
        checkingIssueList.clear()
    }

    fun checkIssue(issue: Issue) {
        checkingIssueList.add(issue)
    }

    fun uncheckIssue(issue: Issue) {
        checkingIssueList.remove(issue)
    }

    fun swipeIssue(issuePosition: Int) {
        val issueList = _issueListStateFlow.value.apply {
            this[issuePosition].isClamped = true
        }
        _issueListStateFlow.value = issueList.toList()
    }

    fun unswipeIssue(issuePosition: Int) {
        val issueList = _issueListStateFlow.value.toMutableList()
        issueList[issuePosition] = issueList[issuePosition].copy(isClamped = false)
        _issueListStateFlow.value = issueList
    }

    fun closeIssue(issuePosition: Int) {
        println("will close issue $issuePosition")
    }

    fun searchIssue(inputText: String) {
        val issueList = initialIssueList.toMutableList()
        val filteredIssueList = issueList.filter { issue ->
            issue.title.contains(inputText)
        }.toMutableList()

        _issueListStateFlow.value = filteredIssueList
    }

    fun setIsSearchingFlag(isSearching: Boolean) {
        _isSearching = isSearching
        if (_isSearching) unswipeAllIssue()
    }

    fun unswipeAllIssue() {
        val issueList = _issueListStateFlow.value.toMutableList()
        issueList.forEachIndexed { index, issue ->
            if (issue.isClamped) {
                issueList[index] = issue.copy(isClamped = false)
            }
        }
        _issueListStateFlow.value = issueList
    }

    fun showInitialIssueList() {
        val issueList = initialIssueList.toList()
        _issueListStateFlow.value = issueList
    }

}