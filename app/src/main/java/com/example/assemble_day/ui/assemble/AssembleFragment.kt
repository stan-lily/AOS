package com.example.assemble_day.ui.assemble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.CALENDAR_DAY_SIZE
import com.example.assemble_day.databinding.FragmentAssembleBinding
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.ui.common.CalendarUtil.toFormattedString
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssembleFragment : Fragment() {

    private val assembleViewModel: AssembleViewModel by viewModels()
    private lateinit var binding: FragmentAssembleBinding
    private lateinit var monthAdapter: MonthAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assemble, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayInitialCalendar()
        setOnInputTextEventListener()
        setOnResetEventListener()
        setOnSaveAssembleDayEventListener()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { setOnAssembleDay() }
                launch { setOnStartDay() }
                launch { showToastMessage() }
                launch { setOnAssembleDayTitle() }
            }
        }
    }

    private fun displayInitialCalendar() {
        monthAdapter = MonthAdapter { calendarDay ->
            selectAssembleDay(calendarDay)
        }
        binding.rvAssembleDayMonth.adapter = monthAdapter.apply {
            submitCalendarData(assembleViewModel.calendarDataMap)
        }
    }

    private fun setOnInputTextEventListener() {
        binding.etAssembleDayTitle.doAfterTextChanged { text ->
            binding.btnAssembleDayTitle.isEnabled = !(text.isNullOrEmpty() || text.isNullOrBlank())
            assembleViewModel.setAssembleDayTitle(text.toString())
        }
    }

    private fun setOnResetEventListener() {
        binding.tlAssemble.firstActionItem.setOnMenuItemClickListener {
            assembleViewModel.resetAssembleDay()
            binding.clAssembleDayTitle.isVisible = false
            binding.etAssembleDayTitle.text = null
            binding.etAssembleDayTitle.isEnabled = false
            true
        }
    }

    private fun selectAssembleDay(calendarDay: CalendarDay) {
        assembleViewModel.selectCalendarDay(calendarDay)
    }

    private fun showAssembleDayTitleEditText(isStartDay: Boolean) {
        binding.clAssembleDayTitle.isVisible = !isStartDay
        binding.etAssembleDayTitle.isEnabled = !isStartDay
    }

    private suspend fun setOnStartDay() {
        assembleViewModel.startDayStateFlow.collect { selectedStartDay ->
            binding.startDate = selectedStartDay.date
            setOnResetAction(selectedStartDay.date != null)
            if (!selectedStartDay.isAssembleDay && !assembleViewModel.didPreviouslySelectAssembleDay) {
                monthAdapter.notifyItemRangeChanged(
                    0,
                    CALENDAR_DAY_SIZE - 1
                )
            }
        }
    }

    private suspend fun setOnAssembleDay() {
        assembleViewModel.assembleDayStateFlow.collect { selectedAssembleDay ->
            binding.endDate = selectedAssembleDay.date
            showAssembleDayTitleEditText(selectedAssembleDay.date == null)
            if (!selectedAssembleDay.isAssembleDay) {
                updateSelectedAssembleDay(selectedAssembleDay)
            }
        }
    }

    private fun updateSelectedAssembleDay(selectedAssembleDay: CalendarDay) {
        val isSelectedDifferentMonth = assembleViewModel.isSelectedDifferentMonth
        if (isSelectedDifferentMonth) {
            monthAdapter.submitNewCalendarDateList(
                assembleViewModel.previousAssembleDay.toFormattedString(),
                assembleViewModel.copiedPreviousCalendarDayList
            )
        }
        monthAdapter.submitNewCalendarDateList(
            selectedAssembleDay.toFormattedString(),
            assembleViewModel.copiedCalendarDayList
        )
    }

    private fun setOnResetAction(doesStartDayExist: Boolean) {
        binding.tlAssemble.firstActionItem.isEnabled = doesStartDayExist
        if (!doesStartDayExist) monthAdapter.submitCalendarData(assembleViewModel.calendarDataMap)
    }

    private suspend fun showToastMessage() {
        assembleViewModel.selectableAssembleDayFlagSharedFlow.collect { selectableDay ->
            if (!selectableDay) Snackbar.make(
                this.requireView(),
                R.string.snack_bar_unable_select,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private suspend fun setOnAssembleDayTitle() {
        assembleViewModel.assembleDayTitleStateFlow.collect { assembleDayTitle ->
            binding.assembleDayTitle = assembleDayTitle
        }
    }

    private fun setOnSaveAssembleDayEventListener() {
        binding.btnAssembleDayTitle.setOnClickListener {
            assembleViewModel.saveAssembleDay()
        }
    }

}