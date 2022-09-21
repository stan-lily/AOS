package com.example.assemble_day.ui.milestone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.databinding.FragmentMilestoneBinding
import com.example.assemble_day.domain.model.Milestone
import com.example.assemble_day.ui.common.eventListener.MilestoneEventListener
import com.example.assemble_day.ui.milestoneCreation.MilestoneCreationFragment
import kotlinx.coroutines.launch


class MilestoneFragment : Fragment(), MilestoneEventListener {

    private val milestoneViewModel: MilestoneViewModel by viewModels()
    private lateinit var binding: FragmentMilestoneBinding
    private lateinit var milestoneAdapter: MilestoneAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        milestoneAdapter = MilestoneAdapter(this)
        binding.rvMilestone.adapter = milestoneAdapter

        closeMilestoneEditMode()
        createNewMilestone()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { submitMilestoneList() }
            }
        }
    }

    private suspend fun submitMilestoneList() {
        milestoneViewModel.milestoneListStateFlow.collect {
            milestoneAdapter.submitList(it)
        }
    }

    override fun switchToIssueEditMode() {
        binding.tlMilestone.isVisible = false
        binding.tlMilestoneEdit.isVisible = true
        milestoneAdapter.showIssueEditMode()
    }

    override fun checkMilestone(milestone: Milestone) {
        milestoneViewModel.checkMilestone(milestone)
    }

    override fun uncheckMilestone(milestone: Milestone) {
        milestoneViewModel.uncheckMilestone(milestone)
    }

    private fun closeMilestoneEditMode() {
        binding.tlMilestoneEdit.setNavigationOnClickListener {
            binding.tlMilestoneEdit.isVisible = false
            binding.tlMilestone.isVisible = true
            milestoneViewModel.clearCheckingIssueList()
            milestoneAdapter.closeIssueEditMode()
        }
    }

    private fun createNewMilestone() {
        binding.tlMilestone.firstActionItem.setOnMenuItemClickListener {
            val milestoneCreationFragment = MilestoneCreationFragment()
            milestoneCreationFragment.show(parentFragmentManager, null)
            true
        }
    }

}