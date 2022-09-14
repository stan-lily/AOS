package com.example.assemble_day.ui.labelCreate

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
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_HEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_BLACK_INDEX
import com.example.assemble_day.common.Constants.LABEL_FONT_COLOR_WHITE_INDEX
import com.example.assemble_day.databinding.FragmentLabelCreateBinding
import com.example.assemble_day.domain.model.Label
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LabelCreateFragment(private val updateLabelList: () -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentLabelCreateBinding
    private var selectedLabel: Label? = null
    private val isEditing get() = selectedLabel != null
    private val labelViewModel: LabelCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLabelCreateBinding.inflate(inflater, container, false)
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
        setSaveBtnOnClickListener()
        setNavigationIconOnEventListener()
        editSelectedLabel()
        setSecondActionText()
        setFirstActionText()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { enableSaveAction() }
                launch { setLabelBackgroundColor() }
                launch { setLabelBackgroundFontColor() }
                launch { setResetOrDeleteBtnOnClickListener() }
                launch { resetLabelValues() }
                launch { succeedDeleteLabel() }
                launch { succeedCreateLabel() }
                launch { succeedUpdateLabel() }
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

    private fun setSaveBtnOnClickListener() {
        binding.tlLabel.firstActionItem.setOnMenuItemClickListener {
            labelViewModel.runFirstActionItem(selectedLabel)
            true
        }
    }

    private fun setResetOrDeleteBtnOnClickListener() {
        binding.tlLabel.secondActionItem.setOnMenuItemClickListener {
            if (isEditing) {
                labelViewModel.deleteLabel(selectedLabel)
            }
            labelViewModel.resetLabel()
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
            if (color.length == 7 && color[0] == '#') {
                if (validateColorFormat(color)) {
                    labelViewModel.setBackgroundColor(color)
                }
            } else {
                showMessageForWrongColorFormat()
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
            binding.etLabelBackgroundColor.setText(it)
        }
    }

    private suspend fun resetLabelValues() {
        labelViewModel.resetActionStateFlow.collect { doesReset ->
            if (doesReset) {
                binding.etLabelTitle.setText("")
                binding.etLabelDescription.setText("")
                labelViewModel.setBackgroundColor("")
                binding.spLabelBackgroundFontColor.setSelection(LABEL_FONT_COLOR_BLACK_INDEX)
                labelViewModel.completedReset()
            }
        }
    }

    fun setSelectedLabel(label: Label) {
        selectedLabel = label
    }

    private fun setFirstActionText() {
        if (isEditing) {
            binding.tlLabel.firstActionItem.setTitle(R.string.action_item_update)
        } else {
            binding.tlLabel.firstActionItem.setTitle(R.string.action_item_save)
        }
    }

    private fun setSecondActionText() {
        if (isEditing) {
            binding.tlLabel.secondActionItem.setTitle(R.string.action_item_delete)
        } else {
            binding.tlLabel.secondActionItem.setTitle(R.string.action_item_reset)
        }
    }

    private fun editSelectedLabel() {
        selectedLabel?.let { label ->
            binding.etLabelTitle.setText(label.name)
            binding.etLabelDescription.setText(label.description)
            labelViewModel.setBackgroundColor(label.backgroundColor)
            if (label.fontColor == LABEL_FONT_COLOR_BLACK_HEX) {
                binding.spLabelBackgroundFontColor.setSelection(LABEL_FONT_COLOR_BLACK_INDEX)
            } else {
                binding.spLabelBackgroundFontColor.setSelection(LABEL_FONT_COLOR_WHITE_INDEX)
            }
        }
    }

    private suspend fun succeedCreateLabel() {
        labelViewModel.networkCreateSuccessStateFlow.collect { isSucceed ->
            if (isSucceed) {
                updateLabelList.invoke()
                labelViewModel.resetLabel()
            }
        }
    }

    private suspend fun succeedUpdateLabel() {
        labelViewModel.networkUpdateSuccessStateFlow.collect { isSucceed ->
            if (isSucceed) {
                updateLabelList.invoke()
            }
        }
    }

    private suspend fun succeedDeleteLabel() {
        labelViewModel.networkDeleteSuccessStateFlow.collect { isSucceed ->
            if (isSucceed) {
                updateLabelList.invoke()
                labelViewModel.resetLabel()
                selectedLabel = null
                setFirstActionText()
                setSecondActionText()
            }
        }
    }

}