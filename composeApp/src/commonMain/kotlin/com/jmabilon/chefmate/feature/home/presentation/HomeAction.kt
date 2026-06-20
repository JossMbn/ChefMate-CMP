package com.jmabilon.chefmate.feature.home.presentation

sealed interface HomeAction {
    data object OnDismissDialog : HomeAction
    data object OnNewCollectionClick : HomeAction
    data class OnCreateCollection(val collectionName: String) : HomeAction
    data object OnScanRecipeClick : HomeAction
}
