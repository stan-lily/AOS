package com.example.assemble_day.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.assemble_day.R
import com.example.assemble_day.common.Constants.FIRST_ACTION
import com.example.assemble_day.common.Constants.SECOND_ACTION

class CustomToolbar(context: Context, attrs: AttributeSet?) :
    Toolbar(context, attrs) {

    lateinit var firstActionItem: MenuItem
    lateinit var secondActionItem: MenuItem
    private var toolbarTitle: Int
    private var toolbarNavigationIcon: Drawable? = null
    private var firstActionText: Int
    private var secondActionText: Int
    private var firstActionIcon: Drawable? = null
    private var secondActionIcon: Drawable? = null

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
                firstActionIcon = getDrawable(R.styleable.CustomToolbar_firstActionIcon)
                secondActionIcon = getDrawable(R.styleable.CustomToolbar_secondActionIcon)
                setToolbarTitle(toolbarTitle)
                setToolbarNavIcon(toolbarNavigationIcon)
                setActionText(firstActionItem, firstActionText)
                setActionText(secondActionItem, secondActionText)
                setActionIcon(firstActionItem, firstActionIcon)
                setActionIcon(secondActionItem, secondActionIcon)
                setActionItemEnable(firstActionItem, firstActionIcon, firstActionText)
                setActionItemEnable(secondActionItem, secondActionIcon, secondActionText)
            } finally {
                recycle()
            }
        }

    }

    private fun setActionItemEnable(actionItem: MenuItem, actionIcon: Drawable?, actionText: Int) {
        actionItem.isVisible = actionIcon != null || actionText != 0
        actionItem.isEnabled = actionIcon != null || actionText != 0
    }

    fun setActionIcon(actionItem: MenuItem, actionIcon: Drawable?) {
        if (actionIcon != null) {
            actionItem.icon = actionIcon
            actionIcon.setTint(ContextCompat.getColor(context, R.color.white))
        }
        actionItem.isVisible = actionIcon != null
        actionItem.isEnabled = actionIcon != null
    }

    private fun setActionText(actionItem: MenuItem, actionText: Int) {
        if (actionText != 0) actionItem.setTitle(actionText)
        else actionItem.isVisible = false
    }

    private fun setToolbarNavIcon(toolbarNavigationIcon: Drawable?) {
        this.navigationIcon = toolbarNavigationIcon
    }

    private fun setToolbarTitle(toolbarTitle: Int) {
        if (toolbarTitle != 0) this.setTitle(toolbarTitle)
    }

    private fun setMenu() {
        inflateMenu(R.menu.toolbar_menu)
        firstActionItem = menu.getItem(FIRST_ACTION)
        secondActionItem = menu.getItem(SECOND_ACTION)
        firstActionItem.isEnabled = false
        secondActionItem.isEnabled = false
    }

    private fun initToolbarUI() {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.royal_blue))
        this.setTitleTextAppearance(context, R.style.Headline6)
        this.setTitleTextColor(ContextCompat.getColor(context, R.color.white))
    }

}