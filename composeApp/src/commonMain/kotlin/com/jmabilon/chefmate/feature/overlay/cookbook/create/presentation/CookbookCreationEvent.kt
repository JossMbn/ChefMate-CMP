package com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation

sealed interface CookbookCreationEvent {
    data object OnCookbookCreated : CookbookCreationEvent
}
