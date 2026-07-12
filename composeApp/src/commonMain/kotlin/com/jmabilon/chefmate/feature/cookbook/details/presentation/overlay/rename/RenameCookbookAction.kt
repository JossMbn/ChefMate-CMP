package com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename

sealed interface RenameCookbookAction {
    data class OnCookbookNameChange(val name: String) : RenameCookbookAction
    data object OnRenameCookbookClick : RenameCookbookAction
}
