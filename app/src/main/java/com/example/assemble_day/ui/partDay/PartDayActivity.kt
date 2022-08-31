package com.example.assemble_day.ui.partDay

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.databinding.ActivityPartDayBinding
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.ui.labelFilter.LabelFilterBottomSheet
import kotlinx.coroutines.launch

class PartDayActivity : AppCompatActivity() {

    private val partDayViewModel: PartDayViewModel by viewModels()
    private lateinit var binding: ActivityPartDayBinding
    private lateinit var labelFilterDialogFragment: LabelFilterBottomSheet
    private val partDayDetailAdapter = PartDayDetailAdapter { position ->
        setTargetLabelOnClickListener(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_part_day)

        val partDayAdapter = PartDayAdapter()
        binding.rvPartDayDetail.adapter = partDayDetailAdapter

        labelFilterDialogFragment = LabelFilterBottomSheet { selectedLabel ->
            if (partDayViewModel.isFiltering) {
                filterLabel(selectedLabel)
            } else {
                updateTargetLabel(selectedLabel)
            }
        }

        setPartDayView(partDayAdapter)
        setChipLabelOnClickListener()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { submitTargetList() }
            }
        }
    }

    private fun setChipLabelOnClickListener() {
        binding.chipPartDayLabel.setOnClickListener {
            partDayViewModel.setFilteringFlag(isFiltering = true)
            val toolbarTitle = resources.getString(R.string.label_filter)
            labelFilterDialogFragment.setToolbarTitle(toolbarTitle)
            labelFilterDialogFragment.show(supportFragmentManager, null)
        }
    }

    private suspend fun submitTargetList() {
        partDayViewModel.loadedTargetStateFlow.collect {
            partDayDetailAdapter.submitList(it)
        }
    }

    private fun setPartDayView(partDayAdapter: PartDayAdapter) {
        binding.rvPartDay.adapter = partDayAdapter.apply {
            submitList(partDayViewModel.loadedAssembleDayUnit)
        }
    }

    private fun filterLabel(selectedLabel: Label?) {
        binding.chipPartDayLabel.text =
            selectedLabel?.name ?: resources.getString(R.string.label_filter_default)
        partDayViewModel.filterLabel(selectedLabel)
    }

    private fun setTargetLabelOnClickListener(selectedPosition: Int) {
        partDayViewModel.setFilteringFlag(isFiltering = false)
        labelFilterDialogFragment.show(supportFragmentManager, null)
        val toolbarTitle = resources.getString(R.string.label_select)
        labelFilterDialogFragment.setToolbarTitle(toolbarTitle)
        partDayViewModel.selectTarget(selectedPosition)
    }

    private fun updateTargetLabel(selectedLabel: Label?) {
        partDayViewModel.updateTargetLabel(selectedLabel)
    }

}