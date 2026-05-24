package com.jmabilon.chefmate.feature.recipe.creation.presentation

sealed interface ManualRecipeCreationAction {
    // Common Actions
    data object OnCreateRecipeClick : ManualRecipeCreationAction

    // Recipe Info Actions
    data class OnTitleChange(val newTitle: String) : ManualRecipeCreationAction
    data class OnImageChange(val newImage: List<Byte>?) : ManualRecipeCreationAction
    data class OnPrepTimeChange(val newPrepTimeHour: Int, val newPrepTimeMinute: Int) :
        ManualRecipeCreationAction

    data class OnCookTimeChange(val newCookTimeHour: Int, val newCookTimeMinute: Int) :
        ManualRecipeCreationAction

    data class OnServingsChange(val newServings: String) : ManualRecipeCreationAction
    data class OnDifficultyChange(val newDifficulty: Int) : ManualRecipeCreationAction
    data class OnSourceUrlChange(val newSourceUrl: String) : ManualRecipeCreationAction

    // Ingredients Sections Actions
    data class OnCreateIngredientSection(val newSectionName: String) : ManualRecipeCreationAction
    data class OnRemoveIngredientSectionClick(val sectionId: String) : ManualRecipeCreationAction
    data class OnRenameIngredientSectionName(val sectionId: String, val newSectionName: String) :
        ManualRecipeCreationAction

    // Ingredient Actions
    data class OnCreateOrEditMainIngredient(
        val ingredientId: String?,
        val name: String,
        val quantity: String,
        val unit: String,
        val note: String
    ) : ManualRecipeCreationAction

    data class OnAddSectionIngredient(
        val sectionId: String,
        val name: String,
        val quantity: String,
        val unit: String,
        val note: String
    ) : ManualRecipeCreationAction

    data class OnEditSectionIngredient(
        val sectionId: String,
        val ingredientId: String,
        val name: String,
        val quantity: String,
        val unit: String,
        val note: String
    ) : ManualRecipeCreationAction

    data class OnRemoveIngredient(val ingredientId: String, val sectionId: String? = null) :
        ManualRecipeCreationAction

    // Instruction Actions
    data class OnCreateOrEditInstruction(
        val instructionId: String?,
        val title: String,
        val instruction: String
    ) : ManualRecipeCreationAction

    data class OnRemoveInstruction(val instructionId: String) : ManualRecipeCreationAction

    // Dialog Actions
    data object OnDismissDialog : ManualRecipeCreationAction
    data class OnShowCreateOrEditIngredientSectionNameDialog(val sectionId: String? = null) :
        ManualRecipeCreationAction

    data class OnShowCreateOrEditMainIngredientDialog(val ingredientId: String? = null) :
        ManualRecipeCreationAction

    data class OnShowCreateOrEditSectionIngredientDialog(
        val sectionId: String,
        val ingredientId: String? = null
    ) : ManualRecipeCreationAction

    data class OnShowCreateOrEditInstructionDialog(val instructionId: String? = null) :
        ManualRecipeCreationAction
}
