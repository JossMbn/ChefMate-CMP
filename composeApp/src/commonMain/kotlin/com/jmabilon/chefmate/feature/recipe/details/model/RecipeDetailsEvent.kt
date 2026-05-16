package com.jmabilon.chefmate.feature.recipe.details.model

sealed interface RecipeDetailsEvent {
    data object RecipeSuccessfullyDeleted : RecipeDetailsEvent
}
