package com.jmabilon.chefmate.domain.recipe.mapper

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeUiData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class RecipeUiDomainMapper : Mapper<RecipeDomain, RecipeUiData> {

    override fun convert(input: RecipeUiData): RecipeDomain {
        return RecipeDomain(
            id = Uuid.random().toString(),
            title = input.info.title,
            imageUrl = null,
            prepTime = input.info.prepTime?.toSecondOfDay() ?: 0,
            cookTime = input.info.cookTime?.toSecondOfDay() ?: 0,
            servings = input.info.servings.toInt(),
            difficulty = input.info.difficulty,
            mainIngredients = input.mainIngredients.map {
                RecipeIngredientDomain(
                    name = it.name,
                    quantity = it.quantity.toDouble(),
                    unit = it.unit,
                    preparationNotes = it.notes,
                    sortOrder = it.orderIndex
                )
            },
            ingredientSections = input.ingredientSections.map { section ->
                RecipeIngredientSectionDomain(
                    name = section.name,
                    ingredients = section.ingredients.map {
                        RecipeIngredientDomain(
                            name = it.name,
                            quantity = it.quantity.toDouble(),
                            unit = it.unit,
                            preparationNotes = it.notes,
                            sortOrder = it.orderIndex
                        )
                    },
                    sortOrder = section.orderIndex
                )
            },
            instructions = input.instructions.map {
                RecipeInstructionDomain(
                    title = it.title,
                    instructions = it.instruction,
                    cookDuration = null,
                    temperature = null,
                    sortOrder = it.orderIndex
                )
            },
            collections = emptyList()
        )
    }
}

fun RecipeUiData.toDomain(): RecipeDomain = RecipeUiDomainMapper().convert(this)
