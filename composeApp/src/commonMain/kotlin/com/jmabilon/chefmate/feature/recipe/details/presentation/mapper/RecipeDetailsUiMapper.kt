package com.jmabilon.chefmate.feature.recipe.details.presentation.mapper

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_easy_level
import chefmate.composeapp.generated.resources.recipe_hard_level
import chefmate.composeapp.generated.resources.recipe_medium_level
import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.DifficultyInfoUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientGroupUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientItemUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeDetailsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.StepUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.TimeInfoUiModel
import kotlinx.collections.immutable.toImmutableList

class RecipeDetailsUiMapper : Mapper<RecipeDetailsUiModel, RecipeDomain> {

    override fun convert(input: RecipeDomain): RecipeDetailsUiModel {
        return RecipeDetailsUiModel(
            id = input.id,
            title = input.title,
            imageUrl = input.imageUrl,
            timeInfo = input.toTimeInfoUiModel(),
            difficultyInfo = input.toDifficultyInfoUiModel(),
            ingredients = input.toIngredientsUiModel(),
            instructions = input.toInstructionsUiModel()
        )
    }

    private fun RecipeDomain.toTimeInfoUiModel(): TimeInfoUiModel {
        val prepTimeUiText = this.prepTime.formatDuration()
        val cookTimeUiText = this.cookTime.formatDuration()

        return TimeInfoUiModel(
            prepTimeText = prepTimeUiText,
            cookTimeText = cookTimeUiText
        )
    }

    private fun RecipeDomain.toDifficultyInfoUiModel(): DifficultyInfoUiModel {
        val difficulty = when (this.difficulty) {
            RecipeDifficulty.Easy -> UiText.ResourceString(Res.string.recipe_easy_level)
            RecipeDifficulty.Medium -> UiText.ResourceString(Res.string.recipe_medium_level)
            RecipeDifficulty.Hard -> UiText.ResourceString(Res.string.recipe_hard_level)
            else -> null
        }

        return DifficultyInfoUiModel(difficulty = difficulty)
    }

    private fun RecipeDomain.toIngredientsUiModel(): IngredientsUiModel {
        val mainIngredientItems = this.mainIngredients.map { it.toIngredientItemUiModel() }
        val allGroups = this.ingredientSections.map { section ->
            IngredientGroupUiModel(
                title = section.name,
                items = section.ingredients
                    .map { it.toIngredientItemUiModel() }
                    .toImmutableList()
            )
        }

        val (untitledGroups, titledGroups) = allGroups.partition { it.title == null }

        val untitledItems = mainIngredientItems + untitledGroups.flatMap { it.items }
        val mergedUntitledGroup = if (untitledItems.isNotEmpty()) {
            listOf(
                IngredientGroupUiModel(
                    title = null,
                    items = untitledItems.toImmutableList()
                )
            )
        } else {
            emptyList()
        }

        val groups = (mergedUntitledGroup + titledGroups).toImmutableList()

        return IngredientsUiModel(
            servings = servings,
            groups = groups
        )
    }

    private fun RecipeIngredientDomain.toIngredientItemUiModel(): IngredientItemUiModel {
        return IngredientItemUiModel(
            id = this.id,
            baseQuantity = this.quantity,
            currentQuantity = this.quantity,
            unit = this.unit,
            ingredientDisplayText = this.name
        )
    }

    private fun RecipeDomain.toInstructionsUiModel(): InstructionsUiModel {
        val steps = this.instructions.map { it.toStepUiModel() }.toImmutableList()

        return InstructionsUiModel(
            steps = steps
        )
    }

    private fun RecipeInstructionDomain.toStepUiModel(): StepUiModel {
        return StepUiModel(
            number = this.sortOrder.toString(),
            title = this.title,
            instruction = this.instructions
        )
    }
}

// =================================================================================================
// Extension
// =================================================================================================

fun RecipeDomain.toRecipeDetailsUiModel(): RecipeDetailsUiModel {
    return RecipeDetailsUiMapper().convert(this)
}
