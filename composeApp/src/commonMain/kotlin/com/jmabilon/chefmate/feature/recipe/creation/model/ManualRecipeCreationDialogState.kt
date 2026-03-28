package com.jmabilon.chefmate.feature.recipe.creation.model

sealed interface ManualRecipeCreationDialogState {
    data class CreateOrEditIngredientSectionName(
        val sectionId: String? = null,
        val sectionName: String? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditMainIngredient(
        val ingredient: RecipeIngredientUiData? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditIngredient(
        val sectionId: String,
        val sectionName: String,
        val ingredient: RecipeIngredientUiData? = null
    ) : ManualRecipeCreationDialogState

    data class CreateOrEditInstruction(
        val instruction: RecipeInstructionUiData? = null
    ) : ManualRecipeCreationDialogState
}
