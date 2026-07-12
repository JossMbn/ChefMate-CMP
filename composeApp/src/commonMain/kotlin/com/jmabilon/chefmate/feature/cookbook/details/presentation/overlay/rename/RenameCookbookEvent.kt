package com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename

sealed interface RenameCookbookEvent {
    data object CookbookSuccessfullyRenamed : RenameCookbookEvent
}
