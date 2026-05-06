package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientSectionDto(
    val id: String,
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
