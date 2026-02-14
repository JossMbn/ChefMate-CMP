package com.jmabilon.chefmate.data.recipe.source.remote.request

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import com.jmabilon.chefmate.domain.recipe.model.TemperatureUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateInstructionRequest(
    val title: String,
    val instructions: String,
    @SerialName("cook_duration_seconds")
    val cookDurationSeconds: Int? = null,
    @SerialName("temperature_value")
    val temperatureValue: Int? = null,
    @SerialName("temperature_unit")
    val temperatureUnit: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int
)

// =================================================================================================
// Request Mapper
// =================================================================================================

class CreateInstructionRequestMapper : Mapper<CreateInstructionRequest, RecipeInstructionDomain> {

    override fun convert(input: RecipeInstructionDomain): CreateInstructionRequest {
        return CreateInstructionRequest(
            title = input.title,
            instructions = input.instructions,
            cookDurationSeconds = input.cookDuration,
            temperatureValue = input.temperature?.value,
            temperatureUnit = TemperatureUnit.toValue(input.temperature?.unit),
            sortOrder = input.sortOrder
        )
    }
}

// =================================================================================================
// Request Mapper Extensions
// =================================================================================================

fun List<RecipeInstructionDomain>.toRequest(): List<CreateInstructionRequest> {
    return CreateInstructionRequestMapper().convert(this)
}
