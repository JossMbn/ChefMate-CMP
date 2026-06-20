package com.jmabilon.chefmate.feature.recipe.details.presentation

sealed interface RecipeDetailsEvent {
    data object RecipeSuccessfullyDeleted : RecipeDetailsEvent
}
