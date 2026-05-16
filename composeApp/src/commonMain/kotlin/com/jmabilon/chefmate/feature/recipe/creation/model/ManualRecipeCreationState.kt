package com.jmabilon.chefmate.feature.recipe.creation.model

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationUiModel

@Stable
data class ManualRecipeCreationState(
    val context: ManualRecipeCreationContext = ManualRecipeCreationContext.Edition,
    val isCreatingRecipe: Boolean = false,
    val recipe: RecipeCreationUiModel = RecipeCreationUiModel(),
    val dialogState: ManualRecipeCreationDialogState? = null
)
