package com.example.assemble_day.ui.labelFilter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.common.Constants.BOTTOM_SHEET_HEIGHT_RATIO
import com.example.assemble_day.databinding.FragmentLabelFilterBottomSheetBinding
import com.example.assemble_day.domain.model.Label
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class LabelFilterBottomSheet(private val selectedLabel: (selectedLabel: Label?) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLabelFilterBottomSheetBinding
    private lateinit var labelFilterAdapter: LabelFilterAdapter
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
            }
        }
    }

    private fun setOnIconClickEventListener() {
        binding.tlLabelSearch.setNavigationOnClickListener {
            dismiss()
        }

        binding.tlLabelSearch.setOnMenuItemClickListener {
            binding.tlLabelSearch.visibility = View.INVISIBLE
            binding.clLabelFilterSearch.isVisible = true
            true
        }

        binding.btnLabelFilterSearchBack.setOnClickListener {
            binding.clLabelFilterSearch.visibility = View.INVISIBLE
            binding.tlLabelSearch.isVisible = true
        }
    }

    private suspend fun submitLabelList() {
        labelFilterViewModel.labelStateFlow.collect {
            labelFilterAdapter.submitList(it.toList())
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.etLabelFilterSearch.setText("")
        selectedLabel(labelFilterViewModel.selectedLabel)
        labelFilterViewModel.initSelectedLabel()
    }
}