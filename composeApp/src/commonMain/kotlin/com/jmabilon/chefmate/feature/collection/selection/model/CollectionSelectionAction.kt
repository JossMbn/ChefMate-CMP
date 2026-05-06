package com.jmabilon.chefmate.feature.collection.selection.model

sealed interface CollectionSelectionAction {

    data class OnCollectionClicked(val collectionId: String) : CollectionSelectionAction

    data object OnConfirmClick : CollectionSelectionAction
}
