package com.example.assemble_day.domain.model

sealed class TargetItemSelection {
    object LabelSection : TargetItemSelection()
    object TargetSelection : TargetItemSelection()
    data class TargetUpdateBtnSelection(val updatedTitle: String) : TargetItemSelection()
    object TargetDeleteBtnSelection : TargetItemSelection()
}


