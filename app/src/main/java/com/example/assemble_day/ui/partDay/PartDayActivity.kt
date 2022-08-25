package com.example.assemble_day.ui.partDay

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.assemble_day.R
import com.example.assemble_day.databinding.ActivityPartDayBinding

class PartDayActivity : AppCompatActivity() {

    private val partDayViewModel: PartDayViewModel by viewModels()
    private lateinit var binding: ActivityPartDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_part_day)
    }

    override fun onStart() {
        super.onStart()
        val partdayAdapter = PartDayAdapter()
        val partDayDetailAdapter = PartDayDetailAdapter()

        binding.rvPartDay.adapter = partdayAdapter.apply {
            submitList(partDayViewModel.loadedAssembleDayUnit)
        }
        binding.rvPartDayDetail.adapter = partDayDetailAdapter.apply {
            submitList(partDayViewModel.dummyTargets)
        }
    }

}