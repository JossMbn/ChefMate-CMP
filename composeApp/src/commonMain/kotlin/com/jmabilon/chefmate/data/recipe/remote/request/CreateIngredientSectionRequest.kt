package com.jmabilon.chefmate.data.recipe.remote.request

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeIngredientSectionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIngredientSectionRequest(
    val name: String,
    @SerialName("sort_order")
    val sortOrder: Int,
    val ingredients: List<CreateIngredientRequest> = emptyList()
)

// =================================================================================================
// Request Mapper
// =================================================================================================

class CreateSectionRequestMapper : Mapper<CreateIngredientSectionRequest, RecipeIngredientSectionDomain> {

    override fun convert(input: RecipeIngredientSectionDomain): CreateIngredientSectionRequest {
        return CreateIngredientSectionRequest(
            name = input.name,
            sortOrder = input.sortOrder,
            ingredients = input.ingredients.toRequest()
        )
    }
}

// =================================================================================================
// Request Mapper Extensions
// =================================================================================================

fun List<RecipeIngredientSectionDomain>.toRequest(): List<CreateIngredientSectionRequest> {
    return CreateSectionRequestMapper().convert(this)
}
