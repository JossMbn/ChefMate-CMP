package com.jmabilon.chefmate.feature.cookbook.selection.presentation

sealed interface CookbookSelectionAction {

    data class OnCookbookClicked(val cookbookId: String) : CookbookSelectionAction

    data object OnConfirmClicked : CookbookSelectionAction

    data object OnCookbookSelectionClicked: CookbookSelectionAction

    data object OnDismissDialog : CookbookSelectionAction
}
