package com.example.assemble_day.ui.issue

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.assemble_day.databinding.FragmentIssueBinding
import com.example.assemble_day.domain.model.Issue
import com.example.assemble_day.ui.common.eventListener.IssueEventListener
import com.example.assemble_day.ui.common.eventListener.SwipeEventListener
import kotlinx.coroutines.launch


class IssueFragment : Fragment(), IssueEventListener, SwipeEventListener {

    private lateinit var binding: FragmentIssueBinding
    private lateinit var issueAdapter: IssueAdapter
    private val issueViewModel: IssueViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        issueAdapter = IssueAdapter(this)
        binding.rvIssue.adapter = issueAdapter

        closeIssueEditMode()
        setIssueOnSwipeEventListener()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { submitIssueList() }
            }
        }
    }

    private suspend fun submitIssueList() {
        issueViewModel.issueListStateFlow.collect { issueList ->
            issueAdapter.submitList(issueList)
        }
    }

    override fun switchToIssueEditMode() {
        binding.tlIssue.isVisible = false
        binding.tlIssueEdit.isVisible = true
        issueAdapter.showIssueEditMode()
    }

    override fun checkIssue(issue: Issue) {
        issueViewModel.checkIssue(issue)
    }

    override fun uncheckIssue(issue: Issue) {
        issueViewModel.uncheckIssue(issue)
    }

    override fun closeIssue(issuePosition: Int) {
        issueViewModel.closeIssue(issuePosition)
    }

    private fun closeIssueEditMode() {
        binding.tlIssueEdit.setNavigationOnClickListener {
            binding.tlIssueEdit.isVisible = false
            binding.tlIssue.isVisible = true
            issueViewModel.clearCheckingIssueList()
            issueAdapter.closeIssueEditMode()
        }
    }

    private fun setIssueOnSwipeEventListener() {
        val swipeHelper = IssueSwipeHelper(this)
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.rvIssue)
    }

    override fun clampItem(itemPosition: Int) {
        issueViewModel.swipeIssue(itemPosition)
    }

    override fun unclampItem(itemPosition: Int) {
        issueViewModel.unswipeIssue(itemPosition)
    }

}