package com.jmabilon.chefmate.feature.recipe.details.presentation

import com.jmabilon.chefmate.core.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeDetailsUiModel

data class RecipeDetailsState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val recipeDetails: RecipeDetailsUiModel = RecipeDetailsUiModel()
)
