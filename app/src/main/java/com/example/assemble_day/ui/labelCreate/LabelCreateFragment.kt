package com.example.assemble_day.ui.labelCreate

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.RGB_BLUE
import com.example.assemble_day.common.Constants.RGB_GREEN
import com.example.assemble_day.common.Constants.RGB_RED
import com.example.assemble_day.databinding.FragmentLabelBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LabelCreateFragment(private val saveLabel: () -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentLabelBinding
    private val labelViewModel: LabelCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        binding.clLabel.minHeight = getWindowHeight()
        binding.clLabel.minWidth = getDialogFragmentDefaultWidth()
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
        isCancelable = false

        setInputTextOnEventListener()
        setFontColorSpinner()
        setBackgroundColorBtnOnEventListener()
        setSaveBtnOnEventListener()
        setNavigationIconOnEventListener()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { enableSaveAction() }
                launch { setLabelBackgroundColor() }
                launch { setLabelBackgroundFontColor() }
            }

        }

    }

    private suspend fun enableSaveAction() {
        labelViewModel.saveActionStateFlow.collect {
            binding.tlLabel.firstActionItem.isEnabled = it
        }
    }

    private fun setNavigationIconOnEventListener() {
        binding.tlLabel.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun setSaveBtnOnEventListener() {
        binding.tlLabel.firstActionItem.setOnMenuItemClickListener {
            labelViewModel.saveLabel()
            saveLabel.invoke()
//            dismiss()
            true
        }
    }

    private fun setInputTextOnEventListener() {
        binding.etLabelTitle.doAfterTextChanged {
            val title = it.toString()
            labelViewModel.setTitle(title)
//                showMessageForWrongTitle()
        }

        binding.etLabelDescription.doAfterTextChanged {
            labelViewModel.setDescription(it.toString())
        }

        binding.etLabelBackgroundColor.doAfterTextChanged {
            val color = it.toString()
            if (color.length == 7) {
                if (validateColorFormat(color)) {
                    labelViewModel.setBackgroundColor(color)
                } else {
                    showMessageForWrongColorFormat()
                }
            }
            if (color.isBlank() || color.isEmpty()) {
                binding.backgroundColor = color
            }
        }
    }

    private fun validateColorFormat(color: String): Boolean {
        val r = "${color[1]}${color[2]}".toInt(16)
        val g = "${color[3]}${color[4]}".toInt(16)
        val b = "${color[5]}${color[6]}".toInt(16)

        return try {
            Color.rgb(r, g, b)
            true
        } catch (e: Throwable) {
            println(color)
            false
        }
    }

    private fun showMessageForWrongColorFormat() {
        Snackbar.make(requireView(), "정확한 색상을 입력하시기 바랍니다", Snackbar.LENGTH_SHORT).show()
    }

    private fun showMessageForWrongTitle() {
        Snackbar.make(requireView(), "제목에는 공백이 있어서는 안됩니다", Snackbar.LENGTH_SHORT).show()
    }

    private fun setBackgroundColorBtnOnEventListener() {
        binding.btnLabelBackgroundColor.setOnClickListener {
            labelViewModel.createRandomColor()
        }
    }

    private suspend fun setLabelBackgroundFontColor() {
        labelViewModel.fontColorStateFlow.collect {
            binding.fontColor = it
        }
    }

    private fun setFontColorSpinner() {
        val fontColorArray = resources.getStringArray(R.array.label_font_color_array)
        binding.spLabelBackgroundFontColor.adapter = ArrayAdapter(
            this.requireContext(),
            R.layout.item_spinner,
            fontColorArray
        )
        binding.spLabelBackgroundFontColor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemView = view as AppCompatTextView
                    itemView.setPadding(0, 0, 0, 0)
                    itemView.setTextColor(
                        ContextCompat.getColor(
                            this@LabelCreateFragment.requireContext(),
                            R.color.grey01
                        )
                    )
                    labelViewModel.setFontColor(itemView.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private suspend fun setLabelBackgroundColor() {
        labelViewModel.backgroundColorStateFlow.collect {
            binding.backgroundColor = it
        }
    }

}