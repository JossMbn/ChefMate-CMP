package com.jmabilon.chefmate.feature.recipe.creation.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.ManualRecipeCreationContext
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.ManualRecipeCreationDialogState
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe.RecipeCreationUiModel

@Stable
data class ManualRecipeCreationState(
    val context: ManualRecipeCreationContext = ManualRecipeCreationContext.Edition,
    val isCreatingRecipe: Boolean = false,
    val recipe: RecipeCreationUiModel = RecipeCreationUiModel(),
    val dialogState: ManualRecipeCreationDialogState? = null
)
