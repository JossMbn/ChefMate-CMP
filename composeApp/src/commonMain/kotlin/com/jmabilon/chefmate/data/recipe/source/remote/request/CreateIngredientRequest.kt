package com.jmabilon.chefmate.data.recipe.source.remote.request

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIngredientRequest(
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    @SerialName("preparation_notes")
    val preparationNotes: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int
)

// =================================================================================================
// Request Mapper
// =================================================================================================

class CreateIngredientRequestMapper : Mapper<CreateIngredientRequest, RecipeIngredientDomain> {

    override fun convert(input: RecipeIngredientDomain): CreateIngredientRequest {
        return CreateIngredientRequest(
            name = input.name,
            quantity = input.quantity,
            unit = input.unit,
            preparationNotes = input.preparationNotes,
            sortOrder = input.sortOrder
        )
    }
}

// =================================================================================================
// Request Mapper Extensions
// =================================================================================================

fun List<RecipeIngredientDomain>.toRequest(): List<CreateIngredientRequest> {
    return CreateIngredientRequestMapper().convert(this)
}
