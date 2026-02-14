package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeTemperatureDomain
import com.jmabilon.chefmate.domain.recipe.model.TemperatureUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeInstructionDto(
    val id: String,
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

// =============================================================================================
// Mapper
// =============================================================================================

class RecipeInstructionMapper : Mapper<RecipeInstructionDomain, RecipeInstructionDto> {

    override fun convert(input: RecipeInstructionDto): RecipeInstructionDomain {
        val temperature = getRecipeTemperature(input = input)

        return RecipeInstructionDomain(
            title = input.title,
            instructions = input.instructions,
            cookDuration = input.cookDurationSeconds,
            temperature = temperature,
            sortOrder = input.sortOrder,
        )
    }

    private fun getRecipeTemperature(input: RecipeInstructionDto): RecipeTemperatureDomain? {
        if (input.temperatureValue == null || input.temperatureUnit == null) return null

        return RecipeTemperatureDomain(
            value = input.temperatureValue,
            unit = TemperatureUnit.fromValue(input.temperatureUnit)
        )
    }
}

// =============================================================================================
// Extensions
// =============================================================================================

fun List<RecipeInstructionDto>.toDomain(): List<RecipeInstructionDomain> =
    RecipeInstructionMapper().convert(this)
