package com.jmabilon.chefmate.feature.collection.details.presentation.overlay.rename

sealed interface RenameCollectionEvent {
    data object CollectionSuccessfullyRenamed : RenameCollectionEvent
}
