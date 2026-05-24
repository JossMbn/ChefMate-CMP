package com.jmabilon.chefmate.feature.collection.selection.presentation

sealed interface CollectionSelectionAction {

    data class OnCollectionClicked(val collectionId: String) : CollectionSelectionAction

    data object OnConfirmClick : CollectionSelectionAction
}
