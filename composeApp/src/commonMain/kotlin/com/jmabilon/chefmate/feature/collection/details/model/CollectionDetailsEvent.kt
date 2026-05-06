package com.jmabilon.chefmate.feature.collection.details.model

sealed interface CollectionDetailsEvent {
    data object OnCollectionDeleted : CollectionDetailsEvent
}
