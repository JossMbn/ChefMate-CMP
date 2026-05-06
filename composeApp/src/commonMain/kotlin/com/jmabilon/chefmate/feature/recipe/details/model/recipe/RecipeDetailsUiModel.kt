package com.jmabilon.chefmate.feature.recipe.details.model.recipe

data class RecipeDetailsUiModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val timeInfo: TimeInfoUiModel = TimeInfoUiModel(),
    val difficultyInfo: DifficultyInfoUiModel = DifficultyInfoUiModel(),
    val ingredients: IngredientsUiModel = IngredientsUiModel(),
    val instructions: InstructionsUiModel = InstructionsUiModel()
) {

    val isRecipeInfoVisible: Boolean
        get() = timeInfo.prepTimeText != null ||
                timeInfo.cookTimeText != null ||
                difficultyInfo.difficulty != null
}
