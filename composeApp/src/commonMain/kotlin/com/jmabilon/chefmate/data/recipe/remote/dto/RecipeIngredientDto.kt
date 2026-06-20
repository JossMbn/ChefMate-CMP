package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class RecipeIngredientDto(
    val id: String = Uuid.random().toString(),
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    @SerialName("preparation_notes")
    val preparationNotes: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeIngredientMapper : Mapper<RecipeIngredientDomain, RecipeIngredientDto> {

    override fun convert(input: RecipeIngredientDto): RecipeIngredientDomain {
        return RecipeIngredientDomain(
            id = input.id,
            name = input.name,
            quantity = input.quantity,
            unit = input.unit,
            note = input.preparationNotes,
            sortOrder = input.sortOrder
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeIngredientDto>.toDomain(): List<RecipeIngredientDomain> =
    RecipeIngredientMapper().convert(this)
