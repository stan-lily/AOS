package com.example.assemble_day.ui.assemble

import android.content.Intent
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.INTENT_NAME_ASSEMBLE_ID
import com.example.assemble_day.databinding.FragmentAssembleBinding
import com.example.assemble_day.domain.model.CalendarDay
import com.example.assemble_day.ui.partDay.PartDayActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssembleFragment : Fragment() {

    private val assembleViewModel: AssembleViewModel by viewModels()
    private lateinit var binding: FragmentAssembleBinding
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assemble, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

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
                launch { submitCalendarData() }
                launch { setOnViewDetailsEventListener() }
            }
        }
    }

    private fun displayInitialCalendar() {
        monthAdapter = MonthAdapter { calendarDay ->
            selectAssembleDay(calendarDay)
        }
        binding.rvAssembleDayMonth.adapter = monthAdapter
    }

    private suspend fun submitCalendarData() {
        assembleViewModel.calendarDataMapStateFlow.collect {
            monthAdapter.submitCalendarData(it)
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

    private fun setOnViewDetailsEventListener() {
        binding.tlAssemble.secondActionItem.setOnMenuItemClickListener {
            val isAssembleDay = assembleViewModel.assembleDayStateFlow.value.isAssembleDay
            val assembleDay = assembleViewModel.assembleDayStateFlow.value.assembleDay
            if (isAssembleDay && assembleDay != null) {
                val intent = Intent(requireActivity(), PartDayActivity::class.java).apply {
                    putExtra(INTENT_NAME_ASSEMBLE_ID, assembleDay.id)
                }
                startActivity(intent)
            }
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
            /*if (!selectedStartDay.isAssembleDay && !assembleViewModel.didPreviouslySelectAssembleDay) {
                monthAdapter.notifyItemRangeChanged(
                    0,
                    CALENDAR_DAY_SIZE - 1
                )
            }*/
        }
    }

    private suspend fun setOnAssembleDay() {
        assembleViewModel.assembleDayStateFlow.collect { selectedAssembleDay ->
            binding.endDate = selectedAssembleDay.date
            showAssembleDayTitleEditText(selectedAssembleDay.date == null)
            setOnViewDetailsAction(selectedAssembleDay.isAssembleDay)
            /*if (!selectedAssembleDay.isAssembleDay) {
                updateSelectedAssembleDay(selectedAssembleDay)
            }*/
        }
    }

    /*private fun updateSelectedAssembleDay(selectedAssembleDay: CalendarDay) {
        val isSelectedDifferentMonth = assembleViewModel.isSelectedDifferentMonth
        if (isSelectedDifferentMonth) {
            monthAdapter.submitNewCalendarDateList(
                assembleViewModel.previousAssembleDay.date,
                assembleViewModel.copiedPreviousCalendarDayList
            )
        }
        monthAdapter.submitNewCalendarDateList(
            selectedAssembleDay.date,
            assembleViewModel.copiedCalendarDayList
        )
    }*/

    private fun setOnResetAction(doesStartDayExist: Boolean) {
        binding.tlAssemble.firstActionItem.isEnabled = doesStartDayExist
//        if (!doesStartDayExist) monthAdapter.submitCalendarData(assembleViewModel.calendarDataMapStateFlow.value)
    }

    private fun setOnViewDetailsAction(doesSelectAssembleDay: Boolean) {
        binding.tlAssemble.secondActionItem.isEnabled = doesSelectAssembleDay
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