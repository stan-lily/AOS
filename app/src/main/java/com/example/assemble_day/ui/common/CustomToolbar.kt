package com.example.assemble_day.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MenuItem
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.assemble_day.R

class CustomToolbar(context: Context, attrs: AttributeSet?) :
    Toolbar(context, attrs) {

    lateinit var firstActionItem: MenuItem
    lateinit var secondActionItem: MenuItem
    private var toolbarTitle: Int
    private var toolbarNavigationIcon: Drawable? = null
    private var firstActionText: Int
    private var secondActionText: Int

    init {
        setMenu()
        initToolbarUI()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomToolbar,
            0, 0
        ).apply {
            try {
                toolbarTitle = getResourceId(R.styleable.CustomToolbar_toolbarTitle, 0)
                toolbarNavigationIcon = getDrawable(R.styleable.CustomToolbar_toolbarNavigationIcon)
                firstActionText = getResourceId(R.styleable.CustomToolbar_firstActionText, 0)
                secondActionText = getResourceId(R.styleable.CustomToolbar_secondActionText, 0)
                setToolbarTitle(toolbarTitle)
                setToolbarNavIcon(toolbarNavigationIcon)
                setFirstActionText(firstActionText)
                setSecondActionText(secondActionText)
            } finally {
                recycle()
            }
        }

    }

    private fun setSecondActionText(secondActionText: Int) {
        if (secondActionText != 0) secondActionItem.setTitle(secondActionText)
        else secondActionItem.isVisible = false
    }

    private fun setFirstActionText(firstActionText: Int) {
        if (firstActionText != 0) firstActionItem.setTitle(firstActionText)
        else firstActionItem.isVisible = false
    }

    private fun setToolbarNavIcon(toolbarNavigationIcon: Drawable?) {
        this.navigationIcon = toolbarNavigationIcon
    }

    private fun setToolbarTitle(toolbarTitle: Int) {
        if (toolbarTitle != 0) this.setTitle(toolbarTitle)
    }

    private fun setMenu() {
        inflateMenu(R.menu.toolbar_menu)
        firstActionItem = menu.getItem(0)
        secondActionItem = menu.getItem(1)
        firstActionItem.isEnabled = false
        secondActionItem.isEnabled = false
    }

    private fun initToolbarUI() {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
        this.setTitleTextAppearance(context, R.style.Headline6)
        this.setTitleTextColor(ContextCompat.getColor(context, R.color.white))
    }

}