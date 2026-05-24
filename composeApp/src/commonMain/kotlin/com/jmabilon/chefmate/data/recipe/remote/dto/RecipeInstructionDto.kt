package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeInstructionDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class RecipeInstructionDto(
    val id: String = Uuid.random().toString(),
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
        //val temperature = getRecipeTemperature(input = input)

        return RecipeInstructionDomain(
            id = input.id,
            title = input.title,
            instructions = input.instructions,
            sortOrder = input.sortOrder,
        )
    }

    /*private fun getRecipeTemperature(input: RecipeInstructionDto): RecipeTemperatureDomain? {
        if (input.temperatureValue == null || input.temperatureUnit == null) return null

        return RecipeTemperatureDomain(
            value = input.temperatureValue,
            unit = TemperatureUnit.fromValue(input.temperatureUnit)
        )
    }*/
}

// =============================================================================================
// Extensions
// =============================================================================================

fun List<RecipeInstructionDto>.toDomain(): List<RecipeInstructionDomain> =
    RecipeInstructionMapper().convert(this)
