package com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation

sealed interface CookbookCreationAction {

    data class OnCookbookNameChange(val name: String) : CookbookCreationAction

    data object OnCreateCookbookClick : CookbookCreationAction
}
