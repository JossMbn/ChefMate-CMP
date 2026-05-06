package com.jmabilon.chefmate.feature.collection.selection.model

sealed interface CollectionSelectionEvent {

    data object OnUpdateRecipeCollectionsSuccess : CollectionSelectionEvent
}
