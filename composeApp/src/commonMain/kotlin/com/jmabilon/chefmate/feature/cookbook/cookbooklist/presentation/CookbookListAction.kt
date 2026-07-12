package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation

sealed interface CookbookListAction {

    data object OnDismissDialog : CookbookListAction

    data object OnAddCookbookClick : CookbookListAction
}
