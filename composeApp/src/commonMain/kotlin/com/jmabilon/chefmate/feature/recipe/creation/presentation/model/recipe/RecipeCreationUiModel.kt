package com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
sealed interface ImageSource {
    data class Url(val url: String) : ImageSource
    data class ByteArray(val bytes: ImmutableList<Byte>) : ImageSource
}

data class RecipeCreationUiModel(
    val id: String = "",
    val title: String = "",
    val imageSource: ImageSource? = null,
    val prepTime: RecipeTimeCreationUiModel = RecipeTimeCreationUiModel(),
    val cookTime: RecipeTimeCreationUiModel = RecipeTimeCreationUiModel(),
    val servings: String = "",
    val difficulty: Int? = null,
    val sourceUrl: String = "",
    val mainIngredients: ImmutableList<RecipeCreationIngredientUiModel> = persistentListOf(),
    val ingredientSections: ImmutableList<RecipeCreationIngredientSectionUiModel> = persistentListOf(),
    val instructions: ImmutableList<RecipeCreationInstructionUiModel> = persistentListOf(),
    val cookbooks: ImmutableList<String> = persistentListOf()
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCreationUiModelMapper(val imageBytes: List<Byte>) : Mapper<RecipeCreationUiModel, RecipeDomain> {

    override fun convert(input: RecipeDomain): RecipeCreationUiModel {
        val imageSource = makeImageSource(
            imageUrl = input.imageUrl,
            imageBytes = imageBytes
        )

        return RecipeCreationUiModel(
            id = input.id,
            title = input.title,
            imageSource = imageSource,
            prepTime = input.prepTime.toRecipeTimeCreationUiModel(),
            cookTime = input.cookTime.toRecipeTimeCreationUiModel(),
            servings = input.servings.toString(),
            difficulty = input.difficulty?.ordinal,
            sourceUrl = "", // Not implemented now
            mainIngredients = input.mainIngredients.toRecipeCreationIngredientUiModel().toImmutableList(),
            ingredientSections = input.ingredientSections.toRecipeCreationIngredientSectionUiModel()
                .toImmutableList(),
            instructions = input.instructions.toRecipeCreationInstructionUiModel().toImmutableList(),
            cookbooks = input.cookbooks.map { it.id }.toImmutableList()
        )
    }

    fun makeImageSource(imageUrl: String?, imageBytes: List<Byte>): ImageSource? {
        return when {
            imageUrl != null -> ImageSource.Url(url = imageUrl)
            imageBytes.isNotEmpty() -> ImageSource.ByteArray(bytes = imageBytes.toImmutableList())
            else -> null
        }
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun RecipeDomain.toRecipeCreationUiModel(imageBytes: List<Byte>): RecipeCreationUiModel {
    return RecipeCreationUiModelMapper(imageBytes = imageBytes).convert(this)
}
