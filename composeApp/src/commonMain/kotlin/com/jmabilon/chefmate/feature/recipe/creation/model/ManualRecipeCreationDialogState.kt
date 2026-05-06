package com.jmabilon.chefmate.feature.recipe.creation.model

import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationIngredientUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationInstructionUiModel

sealed interface ManualRecipeCreationDialogState {
    data class CreateOrEditIngredientSectionName(
        val sectionId: String? = null,
        val sectionName: String? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditMainIngredient(
        val ingredient: RecipeCreationIngredientUiModel? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditIngredient(
        val sectionId: String,
        val sectionName: String,
        val ingredient: RecipeCreationIngredientUiModel? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditInstruction(
        val instruction: RecipeCreationInstructionUiModel? = null
    ) : ManualRecipeCreationDialogState
}
