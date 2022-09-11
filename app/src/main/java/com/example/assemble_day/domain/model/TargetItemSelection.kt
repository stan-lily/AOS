package com.example.assemble_day.domain.model

sealed class TargetItemSelection {
    object labelSection : TargetItemSelection()
    object targetSelection : TargetItemSelection()
    data class targetUpdateBtnSelection(val updatedTitle: String) : TargetItemSelection()
    object targetDeletBtnSelection : TargetItemSelection()
}


