package com.jmabilon.chefmate.feature.recipe.details.model

import com.jmabilon.chefmate.feature.recipe.details.model.recipe.RecipeServingActionUiModel

sealed interface RecipeDetailsAction {

    data class OnServingsChanged(val action: RecipeServingActionUiModel) : RecipeDetailsAction
}
