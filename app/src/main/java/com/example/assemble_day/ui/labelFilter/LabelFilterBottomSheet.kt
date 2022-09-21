package com.example.assemble_day.ui.labelFilter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.BOTTOM_SHEET_HEIGHT_RATIO
import com.example.assemble_day.databinding.FragmentLabelFilterBottomSheetBinding
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.ui.labelCreation.LabelCreationFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LabelFilterBottomSheet(private val selectedLabel: (selectedLabel: Label?) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLabelFilterBottomSheetBinding
    private lateinit var labelFilterAdapter: LabelFilterAdapter
    private var toolbarTitle = ""
    private val labelFilterViewModel: LabelFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLabelFilterBottomSheetBinding.inflate(inflater, container, false)
        binding.clRootLabelFilter.maxHeight = getBottomSheetDialogDefaultHeight()
        binding.clRootLabelFilter.minHeight = getBottomSheetDialogDefaultHeight()
        return binding.root
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return (getWindowHeight() * BOTTOM_SHEET_HEIGHT_RATIO).toInt()
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = false
        labelFilterAdapter = LabelFilterAdapter { selectedPosition ->
            setOnLabelClickListener(selectedPosition)
        }
        binding.rvLabelFilter.adapter = labelFilterAdapter
        setOnIconClickEventListener()
        searchLabel()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { submitLabelList() }
                launch { setSecondActionItemIcon() }
            }
        }
        binding.tlLabelSearch.title = toolbarTitle
    }

    private fun setOnIconClickEventListener() {
        binding.tlLabelSearch.setNavigationOnClickListener {
            dismiss()
        }

        binding.tlLabelSearch.firstActionItem.setOnMenuItemClickListener {
            binding.tlLabelSearch.visibility = View.INVISIBLE
            binding.clLabelFilterSearch.isVisible = true
            true
        }

        binding.tlLabelSearch.secondActionItem.setOnMenuItemClickListener {
            val isEditing = labelFilterViewModel.isSelectingFlagStateFlow.value
            val labelCreateFragment = LabelCreationFragment {
                updateLabel()
            }
            labelCreateFragment.show(parentFragmentManager, null)
            if (isEditing) {
                labelFilterViewModel.selectedLabel?.let { selectedLabel ->
                    labelCreateFragment.setSelectedLabel(selectedLabel)
                }
            }
            true
        }

        binding.btnLabelFilterSearchBack.setOnClickListener {
            binding.clLabelFilterSearch.visibility = View.INVISIBLE
            binding.tlLabelSearch.isVisible = true
        }
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle = title
    }

    private suspend fun submitLabelList() {
        labelFilterViewModel.labelStateFlow.collect {
            labelFilterAdapter.submitList(it.toList())
        }
    }

    private suspend fun setSecondActionItemIcon() {
        labelFilterViewModel.isSelectingFlagStateFlow.collect { isSelecting ->
            if (isSelecting) {
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_create)
                binding.tlLabelSearch.setActionIcon(binding.tlLabelSearch.secondActionItem, icon)
            } else {
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_no_circle)
                binding.tlLabelSearch.setActionIcon(binding.tlLabelSearch.secondActionItem, icon)
            }
        }
    }

    private fun setOnLabelClickListener(selectedPosition: Int) {
        labelFilterViewModel.selectLabel(selectedPosition)
    }

    private fun searchLabel() {
        binding.etLabelFilterSearch.doAfterTextChanged {
            val inputText = it?.toString() ?: ""
            labelFilterViewModel.searchLabel(inputText)
        }
    }

    private fun updateLabel() {
        labelFilterViewModel.initPreviousSelectedPosition()
        labelFilterViewModel.updateLabel()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.etLabelFilterSearch.setText("")
        selectedLabel(labelFilterViewModel.selectedLabel)
        labelFilterViewModel.initSelectedLabel()
    }
}