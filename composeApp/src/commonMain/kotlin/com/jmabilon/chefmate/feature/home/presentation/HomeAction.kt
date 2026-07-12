package com.jmabilon.chefmate.feature.home.presentation

sealed interface HomeAction {
    data object OnDismissDialog : HomeAction
    data object OnScanRecipeClick : HomeAction
}
