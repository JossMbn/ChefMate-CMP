package com.jmabilon.chefmate.feature.collection.selection.presentation

sealed interface CollectionSelectionEvent {

    data object OnUpdateRecipeCollectionsSuccess : CollectionSelectionEvent
}
