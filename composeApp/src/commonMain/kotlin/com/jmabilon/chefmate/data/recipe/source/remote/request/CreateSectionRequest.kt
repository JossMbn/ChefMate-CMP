package com.jmabilon.chefmate.data.recipe.source.remote.request

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionSectionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSectionRequest(
    val name: String,
    @SerialName("sort_order")
    val sortOrder: Int,
    val instructions: List<CreateInstructionRequest> = emptyList()
)

// =================================================================================================
// Request Mapper
// =================================================================================================

class CreateSectionRequestMapper : Mapper<CreateSectionRequest, RecipeInstructionSectionDomain> {

    override fun convert(input: RecipeInstructionSectionDomain): CreateSectionRequest {
        return CreateSectionRequest(
            name = input.name,
            sortOrder = input.sortOrder,
            instructions = input.instructions.toRequest()
        )
    }
}

// =================================================================================================
// Request Mapper Extensions
// =================================================================================================

fun List<RecipeInstructionSectionDomain>.toRequest(): List<CreateSectionRequest> {
    return CreateSectionRequestMapper().convert(this)
}
