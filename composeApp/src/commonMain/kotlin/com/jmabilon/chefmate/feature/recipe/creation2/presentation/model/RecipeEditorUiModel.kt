package com.jmabilon.chefmate.feature.recipe.creation2.presentation.model

import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image.RecipeImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RecipeEditorUiModel(
    val image: RecipeImage = RecipeImage.None,
    val title: String = "",
    val time: TimeSectionUiModel = TimeSectionUiModel(),
    val serves: String = "1",
    val difficulty: RecipeEditorDifficultyUiModel? = null,
    val sourceUrl: String? = null,
    val ingredients: ImmutableList<IngredientsSectionUiModel> = persistentListOf(),
    val steps: ImmutableList<StepUiModel> = persistentListOf()
)

// =================================================================================================
// Time
// =================================================================================================

data class TimeSectionUiModel(
    val prepTime: TimeUiModel = TimeUiModel(),
    val cookTime: TimeUiModel = TimeUiModel()
)

data class TimeUiModel(
    val time: UiText = UiText.DynamicString("—"),
    val hour: Int = 0,
    val minute: Int = 0
)

// =================================================================================================
// Difficulty
// =================================================================================================

enum class RecipeEditorDifficultyUiModel {
    Easy,
    Medium,
    Hard
}

// =================================================================================================
// Ingredients
// =================================================================================================

data class IngredientsSectionUiModel(
    val id: String = "",
    val title: String = "",
    val ingredients: ImmutableList<IngredientUiModel> = persistentListOf(),
)

data class IngredientUiModel(
    val id: String = "",
    val sectionId: String = "",
    val name: String = "",
    val quantity: String? = null,
    val unit: UiText? = null
)

// =================================================================================================
// Steps
// =================================================================================================

data class StepUiModel(
    val id: String = "",
    val instructions: String = ""
)
