package com.jmabilon.chefmate.feature.recipe.creation.presentation

sealed interface ManualRecipeCreationEvent {
    data object RecipeSuccessfullyCreatedOrUpdated : ManualRecipeCreationEvent
}
