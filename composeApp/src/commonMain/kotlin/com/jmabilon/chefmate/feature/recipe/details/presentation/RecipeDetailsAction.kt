package com.jmabilon.chefmate.feature.recipe.details.presentation

import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionUiModel

sealed interface RecipeDetailsAction {

    data class OnServingsChanged(val action: RecipeServingActionUiModel) : RecipeDetailsAction

    data object OnDeleteRecipeClick : RecipeDetailsAction
}
