package com.jmabilon.chefmate.feature.collection.details.model

sealed interface CollectionDetailsAction {
    data object OnDeleteCollectionClick : CollectionDetailsAction
}
