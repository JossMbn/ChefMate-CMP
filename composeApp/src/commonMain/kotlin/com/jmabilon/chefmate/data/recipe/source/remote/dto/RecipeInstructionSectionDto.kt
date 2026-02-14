package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionSectionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeInstructionSectionDto(
    val id: String,
    val name: String,
    val instructions: List<RecipeInstructionDto>,
    @SerialName("sort_order")
    val sortOrder: Int
)

// =============================================================================================
// Mapper
// =============================================================================================

class RecipeInstructionSectionMapper :
    Mapper<RecipeInstructionSectionDomain, RecipeInstructionSectionDto> {

    override fun convert(input: RecipeInstructionSectionDto): RecipeInstructionSectionDomain =
        RecipeInstructionSectionDomain(
            name = input.name,
            sortOrder = input.sortOrder,
            instructions = input.instructions.toDomain()
        )
}

// =============================================================================================
// Extensions
// =============================================================================================

fun List<RecipeInstructionSectionDto>.toDomain(): List<RecipeInstructionSectionDomain> =
    RecipeInstructionSectionMapper().convert(this)
