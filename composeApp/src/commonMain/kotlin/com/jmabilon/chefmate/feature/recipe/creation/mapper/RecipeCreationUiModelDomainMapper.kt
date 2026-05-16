package com.jmabilon.chefmate.feature.recipe.creation.mapper

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeCollectionInfoDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationIngredientSectionUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationIngredientUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationInstructionUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationUiModel
import kotlinx.datetime.LocalTime

class RecipeCreationUiModelDomainMapper : Mapper<RecipeDomain, RecipeCreationUiModel> {

    override fun convert(input: RecipeCreationUiModel): RecipeDomain {
        val prepTime = if (
            input.prepTime.hour != null &&
            input.prepTime.minute != null
        ) LocalTime(input.prepTime.hour, input.prepTime.minute).toSecondOfDay() else 0
        val cookTime = if (
            input.cookTime.hour != null &&
            input.cookTime.minute != null
        ) LocalTime(input.cookTime.hour, input.cookTime.minute).toSecondOfDay() else 0

        return RecipeDomain(
            id = input.id,
            title = input.title,
            imageUrl = null,
            prepTime = prepTime,
            cookTime = cookTime,
            servings = input.servings.toInt(),
            difficulty = RecipeDifficulty.fromUiValue(input.difficulty),
            mainIngredients = input.mainIngredients.toRecipeIngredientDomain(),
            ingredientSections = input.ingredientSections.toRecipeIngredientSectionDomain(),
            instructions = input.instructions.toRecipeInstructionDomain(),
            collections = input.collections.toFakeRecipeCollectionInfoDomain()
        )
    }

    private fun List<RecipeCreationIngredientUiModel>.toRecipeIngredientDomain(): List<RecipeIngredientDomain> =
        this.map {
            RecipeIngredientDomain(
                id = it.id,
                name = it.name,
                quantity = it.quantity.toDouble(),
                unit = it.unit,
                note = it.note,
                sortOrder = it.orderIndex
            )
        }

    private fun List<RecipeCreationIngredientSectionUiModel>.toRecipeIngredientSectionDomain(): List<RecipeIngredientSectionDomain> =
        this.map {
            RecipeIngredientSectionDomain(
                id = it.id,
                name = it.name,
                ingredients = it.ingredients.toRecipeIngredientDomain(),
                sortOrder = it.orderIndex
            )
        }

    private fun List<RecipeCreationInstructionUiModel>.toRecipeInstructionDomain(): List<RecipeInstructionDomain> =
        this.map {
            RecipeInstructionDomain(
                id = it.id,
                title = it.title,
                instructions = it.instruction,
                sortOrder = it.orderIndex
            )
        }

    private fun List<String>.toFakeRecipeCollectionInfoDomain(): List<RecipeCollectionInfoDomain> =
        this.map { collectionId ->
            RecipeCollectionInfoDomain(
                id = collectionId,
                name = collectionId,
                systemType = null
            )
        }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun RecipeCreationUiModel.toDomain(): RecipeDomain {
    return RecipeCreationUiModelDomainMapper().convert(this)
}
