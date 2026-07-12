package com.jmabilon.chefmate.feature.cookbook.selection.presentation

sealed interface CookbookSelectionEvent {

    data object OnUpdateRecipeCookbooksSuccess : CookbookSelectionEvent
}
