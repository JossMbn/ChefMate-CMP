package com.jmabilon.chefmate.feature.home.model

sealed interface HomeAction {
    data object OnDismissDialog : HomeAction
    data object OnNewCollectionClick : HomeAction
    data class OnCreateCollection(val collectionName: String) : HomeAction
}
