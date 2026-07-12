package com.jmabilon.chefmate.feature.recipe.details.presentation

import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeUiModel

data class RecipeDetailsState(
    val recipe: AsyncState<RecipeUiModel> = AsyncState.Loading,
    val isInFavorites: Boolean = false
)

data class RecipeInternalState(
    val servings: Int? = null
)
