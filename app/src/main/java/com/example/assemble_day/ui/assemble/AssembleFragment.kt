package com.example.assemble_day.ui.assemble

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.assemble_day.R
import com.example.assemble_day.databinding.FragmentAssembleBinding


class AssembleDayFragment : Fragment() {

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
        monthAdapter = MonthAdapter()
        binding.rvAssembleDayMonth.adapter = monthAdapter.apply {
            submitCalendarData(assembleViewModel.calendarDataMap)
        }
    }
}