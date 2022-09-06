package com.example.assemble_day.ui.partDay

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.databinding.ActivityPartDayBinding
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.domain.model.TargetItemSelection
import com.example.assemble_day.ui.common.setEndIconOnClickListener
import com.example.assemble_day.ui.labelFilter.LabelFilterBottomSheet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PartDayActivity : AppCompatActivity() {

    private val partDayViewModel: PartDayViewModel by viewModels()
    private lateinit var binding: ActivityPartDayBinding
    private lateinit var labelFilterDialogFragment: LabelFilterBottomSheet
    private val partDayDetailAdapter by lazy {
        PartDayDetailAdapter { itemSelection, position ->
            when (itemSelection) {
                is TargetItemSelection.labelSection -> {
                    setTargetLabelOnClickListener(position)
                }
                is TargetItemSelection.targetSelection -> {
                    setTargetOnClickListener(position)
                }
                is TargetItemSelection.targetUpdateBtnSelection -> {
                    setTargetUpdateBtnOnClickListener(itemSelection.updatedTitle, position)
                }
                is TargetItemSelection.targetDeletBtnSelection -> {
                    setTargetDeleteBtnOnClickListener(position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_part_day)

        val partDayAdapter = PartDayAdapter()
        binding.rvPartDayDetail.adapter = partDayDetailAdapter

        labelFilterDialogFragment = LabelFilterBottomSheet { selectedLabel ->
            if (partDayViewModel.isFiltering) {
                filterLabel(selectedLabel)
            } else if (partDayViewModel.isCreatingTarget) {
                selectLabelForNewTarget(selectedLabel)
            } else {
                updateTargetLabel(selectedLabel)
            }
        }

        setPartDayView(partDayAdapter)
        setChipLabelOnClickListener()
        setTargetAddBtnOnClickListener()
        setTargetEditTextIconOnClickListener()
        inputTargetTitle()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { submitTargetList() }
                launch { changeTargetTitleInputTextIcon() }
                launch { showMessageForCreatingTargetError() }
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
            submitList(partDayViewModel.loadedPartDayList)
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
        partDayViewModel.selectTargetLabel(selectedPosition)
    }

    private fun setTargetOnClickListener(selectedPosition: Int) {
        partDayViewModel.selectTarget(selectedPosition)
    }

    private fun setTargetUpdateBtnOnClickListener(updatedTitle: String, selectedPosition: Int) {
        partDayViewModel.updateTarget(updatedTitle, selectedPosition)
    }

    private fun setTargetDeleteBtnOnClickListener(selectedPosition: Int) {

    }

    private fun updateTargetLabel(selectedLabel: Label?) {
        partDayViewModel.updateTargetLabel(selectedLabel)
    }

    private fun setTargetAddBtnOnClickListener() {
        binding.btnTargetAdd.setOnClickListener {
            setTargetTextInputEditTextVisible(true)
        }
    }

    private fun setTargetEditTextIconOnClickListener() {
        binding.etTargetAdd.setEndIconOnClickListener {
            if (partDayViewModel.inputTargetTitleStateFlow.value.isBlank() || partDayViewModel.inputTargetTitleStateFlow.value.isEmpty()) {
                setTargetTextInputEditTextVisible(false)
            } else {
                val toolbarTitle = resources.getString(R.string.label_select)
                labelFilterDialogFragment.setToolbarTitle(toolbarTitle)
                labelFilterDialogFragment.show(supportFragmentManager, null)
            }
        }
    }

    private fun inputTargetTitle() {
        binding.etTargetAdd.doAfterTextChanged { inputText ->
            val title = (inputText ?: "").toString()
            partDayViewModel.setTargetTitle(title)
        }
    }

    private suspend fun changeTargetTitleInputTextIcon() {
        partDayViewModel.inputTargetTitleStateFlow.collect { title ->
            binding.isInputtingTitle = title
        }
    }

    private fun selectLabelForNewTarget(selectedLabel: Label?) {
        partDayViewModel.createNewTarget(selectedLabel)
        setTargetTextInputEditTextVisible(false)
    }

    private fun setTargetTextInputEditTextVisible(isVisible: Boolean) {
        if (isVisible) {
            binding.etTargetAdd.isEnabled = true
            binding.btnTargetAdd.visibility = View.INVISIBLE
            binding.etTargetAdd.visibility = View.VISIBLE
        } else {
            binding.etTargetAdd.text = null
            binding.etTargetAdd.isEnabled = false
            binding.btnTargetAdd.visibility = View.VISIBLE
            binding.etTargetAdd.visibility = View.INVISIBLE
        }
    }

    private suspend fun showMessageForCreatingTargetError() {
        partDayViewModel.createTargetFlagSharedFlow.collect { isLabelSelected ->
            if (!isLabelSelected) Snackbar.make(
                binding.clPartDayRoot,
                R.string.snack_bar_unable_create_target,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}