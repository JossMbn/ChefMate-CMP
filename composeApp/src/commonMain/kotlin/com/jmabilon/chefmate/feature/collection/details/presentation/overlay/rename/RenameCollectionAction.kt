package com.jmabilon.chefmate.feature.collection.details.presentation.overlay.rename

sealed interface RenameCollectionAction {
    data class OnSheetStarted(val collectionId: String) : RenameCollectionAction
    data class OnCollectionNameChange(val name: String) : RenameCollectionAction
    data object OnRenameCollectionClick : RenameCollectionAction
}
