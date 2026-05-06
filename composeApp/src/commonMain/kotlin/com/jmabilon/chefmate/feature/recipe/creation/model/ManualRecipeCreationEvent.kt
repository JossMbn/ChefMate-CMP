package com.jmabilon.chefmate.feature.recipe.creation.model

sealed interface ManualRecipeCreationEvent {
    data object RecipeSuccessfullyCreatedOrUpdated : ManualRecipeCreationEvent
}
