package com.jmabilon.chefmate.feature.collection.details.sheet.rename.model

sealed interface RenameCollectionEvent {
    data object CollectionSuccessfullyRenamed : RenameCollectionEvent
}
