package com.example.assemble_day.ui.common.eventListener

import com.example.assemble_day.domain.model.PartDay
import com.example.assemble_day.domain.model.PartDayTarget

interface PartDayEventListener {
    fun selectPartDay(selectedPartDay: PartDay)
    fun dropTargetToOtherPartDay(
        droppedTarget: PartDayTarget,
        droppedTargetPosition: Int,
        itemPosition: Int
    )
}