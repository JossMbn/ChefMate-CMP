package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class RecipeDto(
    val id: String = Uuid.random().toString(),
    @SerialName("user_id")
    val userId: String,
    val title: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("prep_time_seconds")
    val prepTimeSeconds: Int,
    @SerialName("cook_time_seconds")
    val cookTimeSeconds: Int,
    val servings: Int,
    val difficulty: Int? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val ingredients: List<RecipeIngredientDto> = emptyList(),
    @SerialName("ingredient_sections")
    val ingredientSections: List<RecipeIngredientSectionDto> = emptyList(),
    val instructions: List<RecipeInstructionDto> = emptyList(),
    @SerialName("collections")
    val cookbooks: List<RecipeCookbookInfoDto> = emptyList()
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeMapper : Mapper<RecipeDomain, RecipeDto> {

    override fun convert(input: RecipeDto): RecipeDomain {
        return RecipeDomain(
            id = input.id,
            title = input.title,
            imageUrl = input.imageUrl,
            prepTime = input.prepTimeSeconds,
            cookTime = input.cookTimeSeconds,
            servings = input.servings,
            difficulty = RecipeDifficulty.fromDtoValue(input.difficulty),
            mainIngredients = input.ingredients.toDomain(),
            ingredientSections = input.ingredientSections.toDomain(),
            instructions = input.instructions.toDomain(),
            cookbooks = input.cookbooks.toDomain()
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeDto>?.toDomain(): List<RecipeDomain> {
    return RecipeMapper().convertOrEmpty(this)
}
