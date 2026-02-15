package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: String,
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
    val ingredients: List<RecipeIngredientDto>,
    val sections: List<RecipeInstructionSectionDto>,
    val collections: List<RecipeCollectionInfoDto>
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
            difficulty = RecipeDifficulty.fromValue(input.difficulty),
            ingredients = input.ingredients.toDomain(),
            instructionSections = input.sections.toDomain(),
            collections = input.collections.toDomain()
        )
    }
}
