package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class RecipeIngredientSectionDto(
    val id: String = Uuid.random().toString(),
    val name: String,
    val ingredients: List<RecipeIngredientDto>,
    @SerialName("sort_order")
    val sortOrder: Int
)

// =============================================================================================
// Mapper
// =============================================================================================

class RecipeInstructionSectionMapper :
    Mapper<RecipeIngredientSectionDomain, RecipeIngredientSectionDto> {

    override fun convert(input: RecipeIngredientSectionDto): RecipeIngredientSectionDomain =
        RecipeIngredientSectionDomain(
            id = input.id,
            name = input.name,
            sortOrder = input.sortOrder,
            ingredients = input.ingredients.toDomain()
        )
}

// =============================================================================================
// Extensions
// =============================================================================================

fun List<RecipeIngredientSectionDto>.toDomain(): List<RecipeIngredientSectionDomain> =
    RecipeInstructionSectionMapper().convert(this)
