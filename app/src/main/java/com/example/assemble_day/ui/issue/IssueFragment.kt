package com.example.assemble_day.ui.issue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.databinding.FragmentIssueBinding
import kotlinx.coroutines.launch


class IssueFragment : Fragment() {

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
        issueAdapter = IssueAdapter()
        binding.rvIssue.adapter = issueAdapter

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

}