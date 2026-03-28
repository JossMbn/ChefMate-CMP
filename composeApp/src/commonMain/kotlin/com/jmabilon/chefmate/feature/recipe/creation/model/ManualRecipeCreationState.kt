@file:OptIn(ExperimentalUuidApi::class)

package com.jmabilon.chefmate.feature.recipe.creation.model

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class RecipeInstructionUiData(
    val id: String = Uuid.random().toString(),
    val title: String,
    val instruction: String,
    val orderIndex: Int
)

data class RecipeIngredientUiData(
    val id: String = Uuid.random().toString(),
    val name: String,
    val quantity: String,
    val unit: String,
    val notes: String? = null,
    val orderIndex: Int
) {

    val displayText: String
        get() = buildString {
            append(quantity)
            if (unit.isNotEmpty()) append(" $unit")
            append(" $name")
        }
}

data class RecipeIngredientSectionUiData(
    val id: String = Uuid.random().toString(),
    val name: String,
    val ingredients: ImmutableList<RecipeIngredientUiData> = persistentListOf(),
    val orderIndex: Int
)

data class RecipeInfoUiData(
    val title: String = "",
    val image: ImmutableList<Byte>? = null,
    val prepTime: LocalTime? = null,
    val cookTime: LocalTime? = null,
    val servings: String = "",
    val difficulty: RecipeDifficulty? = null,
    val sourceUrl: String = ""
)

data class RecipeUiData(
    val info: RecipeInfoUiData = RecipeInfoUiData(),
    val mainIngredients: ImmutableList<RecipeIngredientUiData> = persistentListOf(),
    val ingredientSections: ImmutableList<RecipeIngredientSectionUiData> = persistentListOf(),
    val instructions: ImmutableList<RecipeInstructionUiData> = persistentListOf()
)

@Stable
data class ManualRecipeCreationState(
    val isCreatingRecipe: Boolean = false,
    val recipe: RecipeUiData = RecipeUiData(),
    val dialogState: ManualRecipeCreationDialogState? = null
)
