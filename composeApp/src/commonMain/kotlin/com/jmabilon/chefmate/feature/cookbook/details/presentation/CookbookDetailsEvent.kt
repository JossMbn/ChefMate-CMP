package com.jmabilon.chefmate.feature.cookbook.details.presentation

sealed interface CookbookDetailsEvent {
    data object OnCookbookDeleted : CookbookDetailsEvent
}
