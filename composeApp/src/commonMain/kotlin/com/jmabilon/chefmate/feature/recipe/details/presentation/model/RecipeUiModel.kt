package com.jmabilon.chefmate.feature.recipe.details.presentation.model

import com.jmabilon.chefmate.core.presentation.UiText
import kotlinx.collections.immutable.ImmutableList

data class RecipeUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val quickInfo: QuickInfoUiModel,
    val serving: String,
    val ingredients: ImmutableList<IngredientSectionUiModel>,
    val instructions: ImmutableList<InstructionUiModel>
)

data class QuickInfoUiModel(
    val prepTime: UiText,
    val cookTime: UiText,
    val difficulty: UiText
)

// =================================================================================================
// Ingredients Section
// =================================================================================================

data class IngredientSectionUiModel(
    val title: String,
    val ingredients: ImmutableList<IngredientInfo>
)

data class IngredientInfo(
    val name: String,
    val quantityUnit: String
)

// =================================================================================================
// Instruction Section
// =================================================================================================

data class InstructionUiModel(
    val index: String,
    val instruction: String
)
