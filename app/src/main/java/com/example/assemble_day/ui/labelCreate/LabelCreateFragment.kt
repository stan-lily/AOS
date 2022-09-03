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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                labelViewModel.saveActionStateFlow.collect {
                    binding.tlLabel.firstActionItem.isEnabled = it
                }
            }
        }
        setInputTextOnEventListener()
        setFontColorSpinner()
        setBackgroundColorBtnOnEventListener()
        setSaveBtnOnEventListener()
        setNavigationIconOnEventListener()
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
            dismiss()
            true
        }
    }

    private fun setInputTextOnEventListener() {
        binding.etLabelTitle.doAfterTextChanged {
            labelViewModel.setTitle(it.toString())
        }

        binding.etLabelDescription.doAfterTextChanged {
            labelViewModel.setDescription(it.toString())
        }

        binding.etLabelBackgroundColor.doAfterTextChanged {
            labelViewModel.setBackgroundColor(it.toString())
        }
    }

    private fun setBackgroundColorBtnOnEventListener() {
        binding.btnLabelBackgroundColor.setOnClickListener {
            val rgbArray = labelViewModel.createRandomColor()
            setLabelBackgroundColor(rgbArray)
            setLabelBackgroundText(rgbArray)
        }
    }

    private fun setLabelBackgroundFontColor() {
        val fontColor = labelViewModel.getFontColor()
        binding.tvLabelBackgroundColorPreview.setTextColor(Color.parseColor(fontColor))
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
                    setLabelBackgroundFontColor()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setLabelBackgroundText(rgbArray: Array<Int>) {
        val redHex = Integer.toHexString(rgbArray[RGB_RED]).uppercase()
        val greenHex = Integer.toHexString(rgbArray[RGB_GREEN]).uppercase()
        val blueHex = Integer.toHexString(rgbArray[RGB_BLUE]).uppercase()

        binding.etLabelBackgroundColor.setText("#$redHex$greenHex$blueHex")
    }

    private fun setLabelBackgroundColor(rgbArray: Array<Int>) {
        val red = rgbArray[RGB_RED]
        val green = rgbArray[RGB_GREEN]
        val blue = rgbArray[RGB_BLUE]

        binding.tvLabelBackgroundColorPreview.backgroundTintList =
            ColorStateList.valueOf(Color.rgb(red, green, blue))
    }

}