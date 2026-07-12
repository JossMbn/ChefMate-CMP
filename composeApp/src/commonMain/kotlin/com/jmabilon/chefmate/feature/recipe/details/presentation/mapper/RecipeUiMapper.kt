package com.jmabilon.chefmate.feature.recipe.details.presentation.mapper

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_easy_level
import chefmate.composeapp.generated.resources.recipe_hard_level
import chefmate.composeapp.generated.resources.recipe_medium_level
import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.core.presentation.extension.formatQuantity
import com.jmabilon.chefmate.core.presentation.toAsyncState
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientInfo
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientSectionUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.QuickInfoUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class RecipeUiMapper(
    private val newServings: Int?
) : Mapper<RecipeUiModel, RecipeDomain> {

    override fun convert(input: RecipeDomain): RecipeUiModel {
        return RecipeUiModel(
            id = input.id,
            name = input.title,
            imageUrl = input.imageUrl,
            quickInfo = input.toQuickInfoUiModel(),
            serving = newServings?.toString() ?: input.servings.toString(),
            ingredients = input.toIngredientsSectionsUiModel(),
            instructions = input.toInstructionsUiModel()
        )
    }

    private fun RecipeDomain.toQuickInfoUiModel(): QuickInfoUiModel {
        val defaultValue = UiText.DynamicString("—")
        val prepTimeUiText = this.prepTime.formatDuration() ?: defaultValue
        val cookTimeUiText = this.cookTime.formatDuration() ?: defaultValue
        val difficultyUiText = when (this.difficulty) {
            RecipeDifficulty.Easy -> UiText.ResourceString(Res.string.recipe_easy_level)
            RecipeDifficulty.Medium -> UiText.ResourceString(Res.string.recipe_medium_level)
            RecipeDifficulty.Hard -> UiText.ResourceString(Res.string.recipe_hard_level)
            else -> defaultValue
        }

        return QuickInfoUiModel(
            prepTime = prepTimeUiText,
            cookTime = cookTimeUiText,
            difficulty = difficultyUiText
        )
    }

    // =================================================================================================
    // Ingredients
    // =================================================================================================

    private fun RecipeDomain.toIngredientsSectionsUiModel(): ImmutableList<IngredientSectionUiModel> {
        val ingredientSections = this.ingredientSections.map { ingredientGroup ->
            IngredientSectionUiModel(
                title = ingredientGroup.name.uppercase(),
                ingredients = ingredientGroup.ingredients.map { ingredient ->
                    val quantity = ingredient.quantity
                        .calculateQuantityForCustomServings(this.servings)
                        ?.formatQuantity()

                    IngredientInfo(
                        name = ingredient.name,
                        quantityUnit = "$quantity ${ingredient.unit}"
                    )
                }.toImmutableList()
            )
        }.toMutableList()

        val mainIngredientSection = IngredientSectionUiModel(
            title = "MAIN",
            ingredients = this.mainIngredients.map { ingredient ->
                val quantity = ingredient.quantity
                    .calculateQuantityForCustomServings(this.servings)
                    ?.formatQuantity()

                IngredientInfo(
                    name = ingredient.name,
                    quantityUnit = "$quantity ${ingredient.unit}"
                )
            }.toImmutableList()
        )

        ingredientSections.add(0, mainIngredientSection)


        return ingredientSections
            .filter { it.ingredients.isNotEmpty() }
            .toImmutableList()
    }

    private fun Double?.calculateQuantityForCustomServings(baseServings: Int): Double? {
        val quantity = this ?: return null

        if (newServings == null) return quantity

        return (quantity / baseServings.toDouble()) * newServings
    }

    // =================================================================================================
    // Instructions
    // =================================================================================================

    private fun RecipeDomain.toInstructionsUiModel(): ImmutableList<InstructionUiModel> {
        return this.instructions.map { instruction ->
            InstructionUiModel(
                index = instruction.sortOrder.toString(),
                instruction = instruction.instructions
            )
        }.toImmutableList()
    }
}

fun RecipeDomain?.toAsyncRecipeUiModel(customServings: Int? = null): AsyncState<RecipeUiModel> {
    if (this == null) return AsyncState.Loading

    return RecipeUiMapper(customServings).convert(this).toAsyncState()
}
