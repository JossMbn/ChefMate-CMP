package com.jmabilon.chefmate.feature.recipe.details.presentation

import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionType

sealed interface RecipeDetailsAction {

    data class OnServingsChanged(val action: RecipeServingActionType) : RecipeDetailsAction

    data object OnDeleteRecipeClick : RecipeDetailsAction

    data object OnFavoriteClick : RecipeDetailsAction
}
