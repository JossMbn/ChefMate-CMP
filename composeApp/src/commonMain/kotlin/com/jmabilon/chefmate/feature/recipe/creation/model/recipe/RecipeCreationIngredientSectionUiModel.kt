package com.jmabilon.chefmate.feature.recipe.creation.model.recipe

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class RecipeCreationIngredientSectionUiModel(
    val id: String = Uuid.random().toString(),
    val name: String,
    val ingredients: ImmutableList<RecipeCreationIngredientUiModel> = persistentListOf(),
    val orderIndex: Int
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCreationIngredientSectionUiModelMapper :
    Mapper<RecipeCreationIngredientSectionUiModel, RecipeIngredientSectionDomain> {

    override fun convert(input: RecipeIngredientSectionDomain): RecipeCreationIngredientSectionUiModel {
        return RecipeCreationIngredientSectionUiModel(
            name = input.name,
            ingredients = input.ingredients.toRecipeCreationIngredientUiModel().toImmutableList(),
            orderIndex = input.sortOrder
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeIngredientSectionDomain>.toRecipeCreationIngredientSectionUiModel(): List<RecipeCreationIngredientSectionUiModel> {
    return RecipeCreationIngredientSectionUiModelMapper().convert(this)
}
