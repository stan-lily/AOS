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
import kotlinx.coroutines.launch


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
        initCalendar()
        setOnInputTextEventListener()
        setOnResetEventListener()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { setOnResetAction() }
                launch { setOnAssembleDay() }
                launch { setOnStartDay() }
                launch { showAssembleDayTitle() }
            }
        }
    }

    private fun initCalendar() {
        monthAdapter = MonthAdapter { calendarDay ->
            selectAssembleDay(calendarDay)
        }
        binding.rvAssembleDayMonth.adapter = monthAdapter.apply {
            submitCalendarData(assembleViewModel.calendarDataMap)
        }
    }

    private fun setOnInputTextEventListener() {
        binding.etAssembleDayTitle.doAfterTextChanged { text ->
            if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                binding.btnAssembleDayTitle.isEnabled = false
            } else {
                assembleViewModel.setAssembleDayTitle(text.toString())
                binding.btnAssembleDayTitle.isEnabled = true
                // TODO: 상세 보기 화면 이동
            }
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

    private suspend fun showAssembleDayTitle() {
        assembleViewModel.startDayFlagStateFlow.collect { isStartDay ->
            binding.clAssembleDayTitle.isVisible = !isStartDay
            binding.etAssembleDayTitle.isEnabled = !isStartDay
        }
    }

    private suspend fun setOnStartDay() {
        assembleViewModel.startDayStateFlow.collect {
            binding.startDate = it.date
            monthAdapter.notifyItemRangeChanged(0, CALENDAR_DAY_SIZE - 1)
        }
    }

    private suspend fun setOnAssembleDay() {
        assembleViewModel.assembleDayStateFlow.collect { selectedAssebleDay ->
            binding.endDate = selectedAssebleDay.date
            monthAdapter.submitNewCalendarDateList(
                selectedAssebleDay.toFormattedString(),
                assembleViewModel.copiedCalendarDayList
            )
        }
    }

    private suspend fun setOnResetAction() {
        assembleViewModel.resetActionFlagStateFlow.collect { resetActionEnable ->
            binding.tlAssemble.firstActionItem.isEnabled = resetActionEnable
            if (!resetActionEnable) monthAdapter.submitCalendarData(assembleViewModel.calendarDataMap)
        }
    }

}