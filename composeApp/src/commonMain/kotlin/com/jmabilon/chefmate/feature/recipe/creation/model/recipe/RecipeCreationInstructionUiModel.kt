package com.jmabilon.chefmate.feature.recipe.creation.model.recipe

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class RecipeCreationInstructionUiModel(
    val id: String = Uuid.random().toString(),
    val title: String,
    val instruction: String,
    val orderIndex: Int
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCreationInstructionUiModelMapper :
    Mapper<RecipeCreationInstructionUiModel, RecipeInstructionDomain> {

    override fun convert(input: RecipeInstructionDomain): RecipeCreationInstructionUiModel {
        return RecipeCreationInstructionUiModel(
            id = input.id,
            title = input.title,
            instruction = input.instructions,
            orderIndex = input.sortOrder
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeInstructionDomain>.toRecipeCreationInstructionUiModel(): List<RecipeCreationInstructionUiModel> {
    return RecipeCreationInstructionUiModelMapper().convert(this)
}
