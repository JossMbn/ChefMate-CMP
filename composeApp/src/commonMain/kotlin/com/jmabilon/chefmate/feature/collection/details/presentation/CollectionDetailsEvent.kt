package com.jmabilon.chefmate.feature.collection.details.presentation

sealed interface CollectionDetailsEvent {
    data object OnCollectionDeleted : CollectionDetailsEvent
}
