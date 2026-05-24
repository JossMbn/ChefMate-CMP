package com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class RecipeCreationIngredientUiModel(
    val id: String = Uuid.random().toString(),
    val name: String,
    val quantity: String,
    val unit: String,
    val note: String? = null,
    val orderIndex: Int
) {
    val displayText: String
        get() = buildString {
            append(quantity)
            if (unit.isNotEmpty()) append(" $unit")
            append(" $name")
        }
}

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCreationIngredientUiModelMapper :
    Mapper<RecipeCreationIngredientUiModel, RecipeIngredientDomain> {

    override fun convert(input: RecipeIngredientDomain): RecipeCreationIngredientUiModel {
        return RecipeCreationIngredientUiModel(
            id = input.id,
            name = input.name,
            quantity = input.quantity?.toString() ?: "",
            unit = input.unit ?: "",
            orderIndex = input.sortOrder
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeIngredientDomain>.toRecipeCreationIngredientUiModel(): List<RecipeCreationIngredientUiModel> {
    return RecipeCreationIngredientUiModelMapper().convert(this)
}
