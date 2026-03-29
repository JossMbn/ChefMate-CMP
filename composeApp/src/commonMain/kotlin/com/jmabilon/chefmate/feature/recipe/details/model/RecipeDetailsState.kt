package com.jmabilon.chefmate.feature.recipe.details.model

import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.feature.recipe.details.model.recipe.RecipeDetailsUiModel

data class RecipeDetailsState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val recipeDetails: RecipeDetailsUiModel = RecipeDetailsUiModel()
)
