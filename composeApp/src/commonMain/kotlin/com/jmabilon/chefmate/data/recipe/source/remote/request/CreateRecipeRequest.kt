package com.jmabilon.chefmate.data.recipe.source.remote.request

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeRequest(
    val title: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("prep_time_seconds")
    val prepTimeSeconds: Int,
    @SerialName("cook_time_seconds")
    val cookTimeSeconds: Int,
    val servings: Int = 1,
    val difficulty: Int? = null,
    val ingredients: List<CreateIngredientRequest> = emptyList(),
    @SerialName("ingredient_sections")
    val ingredientSections: List<CreateIngredientSectionRequest> = emptyList(),
    val instructions: List<CreateInstructionRequest>
)

// =================================================================================================
// Request Mapper
// =================================================================================================

class CreateRecipeRequestMapper : Mapper<CreateRecipeRequest, RecipeDomain> {

    override fun convert(input: RecipeDomain): CreateRecipeRequest {
        return CreateRecipeRequest(
            title = input.title,
            imageUrl = input.imageUrl,
            prepTimeSeconds = input.prepTime,
            cookTimeSeconds = input.cookTime,
            servings = input.servings,
            difficulty = RecipeDifficulty.toValue(input.difficulty),
            ingredients = input.mainIngredients.toRequest(),
            ingredientSections = input.ingredientSections.toRequest(),
            instructions = input.instructions.toRequest()
        )
    }
}

// =================================================================================================
// Request Mapper Extensions
// =================================================================================================

fun RecipeDomain.toRequest(): CreateRecipeRequest {
    return CreateRecipeRequestMapper().convert(this)
}
