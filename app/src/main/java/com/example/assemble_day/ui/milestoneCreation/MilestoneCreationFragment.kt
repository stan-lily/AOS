package com.example.assemble_day.ui.milestoneCreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.databinding.FragmentMilestoneCreationBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MilestoneCreationFragment : DialogFragment() {

    private val milestoneCreateViewModel: MilestoneCreationViewModel by viewModels()
    private lateinit var binding: FragmentMilestoneCreationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMilestoneCreationBinding.inflate(inflater, container, false)
        binding.clMilestone.minHeight = getWindowHeight()
        binding.clMilestone.minWidth = getDialogFragmentDefaultWidth()
        return binding.root
    }

    private fun getDialogFragmentDefaultWidth(): Int {
        return (getWindowWidth() * 0.9).toInt()
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return displayMetrics.heightPixels
    }

    private fun getWindowWidth(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return displayMetrics.widthPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDatePicker()
        inputMilestoneTitle()
        inputMilestoneDescription()
        resetMilestone()
        createMilestone()
        dismissMilestoneCreation()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { enableCreateMilestoneBtn() }
            }
        }
    }

    private fun showDatePicker() {
        binding.tvMilestoneDateValue.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(requireContext().resources.getText(R.string.milestone_date))
                    .setCalendarConstraints(constraintsBuilder)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(parentFragmentManager, null)
            selectMilestoneDate(datePicker)
            unselectMilestoneDate(datePicker)
        }
    }

    private fun unselectMilestoneDate(datePicker: MaterialDatePicker<Long>) {
        datePicker.addOnNegativeButtonClickListener {
            binding.tvMilestoneDateValue.text = ""
        }
    }

    private fun selectMilestoneDate(datePicker: MaterialDatePicker<Long>) {
        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate: String = format.format(calendar.time)
            binding.tvMilestoneDateValue.text = formattedDate
            milestoneCreateViewModel.inputCompletedDate(formattedDate)
        }
    }

    private fun inputMilestoneTitle() {
        binding.etMilestoneTitle.doAfterTextChanged { inputText ->
            val text = inputText?.toString() ?: ""
            milestoneCreateViewModel.inputMilestoneTitle(text)
        }
    }

    private fun inputMilestoneDescription() {
        binding.etMilestoneDescription.doAfterTextChanged { inputText ->
            val text = inputText?.toString() ?: ""
            milestoneCreateViewModel.inputMilestoneDescription(text)
        }
    }

    private suspend fun enableCreateMilestoneBtn() {
        milestoneCreateViewModel.milestoneTitleStateFlow.collect { title ->
            binding.tlMilestone.secondActionItem.isEnabled = title.isNotBlank() && title.isNotEmpty()
        }
    }

    private fun resetMilestone() {
        binding.tlMilestone.firstActionItem.setOnMenuItemClickListener {
            binding.etMilestoneTitle.setText("")
            binding.etMilestoneDescription.setText("")
            binding.tvMilestoneDateValue.text = ""
            milestoneCreateViewModel.resetMilestone()
            true
        }
    }

    private fun createMilestone() {
        binding.tlMilestone.secondActionItem.setOnMenuItemClickListener {
            milestoneCreateViewModel.createMilestone()
            true
        }
    }

    private fun dismissMilestoneCreation() {
        binding.tlMilestone.setNavigationOnClickListener {
            dismiss()
        }
    }
}