package com.example.assemble_day.ui.partDay

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.assemble_day.R
import com.example.assemble_day.databinding.ActivityPartDayBinding
import com.example.assemble_day.domain.model.Label
import com.example.assemble_day.ui.labelFilter.LabelFilterBottomSheet

class PartDayActivity : AppCompatActivity() {

    private val partDayViewModel: PartDayViewModel by viewModels()
    private lateinit var binding: ActivityPartDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_part_day)
    }

    override fun onStart() {
        super.onStart()
        val partDayAdapter = PartDayAdapter()
        val partDayDetailAdapter = PartDayDetailAdapter()
        val labelFilterDialogFragment = LabelFilterBottomSheet { selectedLabel ->
            filterLabel(selectedLabel)
        }

        setPartDayView(partDayAdapter)
        setPartDayDetailView(partDayDetailAdapter)
        setChipLabel(labelFilterDialogFragment)
    }

    private fun setChipLabel(labelFilterDialogFragment: LabelFilterBottomSheet) {
        binding.chipPartDayLabel.setOnClickListener {
            labelFilterDialogFragment.show(supportFragmentManager, null)
        }
    }

    private fun setPartDayDetailView(partDayDetailAdapter: PartDayDetailAdapter) {
        binding.rvPartDayDetail.adapter = partDayDetailAdapter.apply {
            submitList(partDayViewModel.dummyTargets)
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

}