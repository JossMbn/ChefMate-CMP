package com.jmabilon.chefmate.designsystem.component.recipe.model

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.designsystem.utils.UiText
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class RecipeCardUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val prepTimeMinute: UiText?,
    val isFavorite: Boolean
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCardUiModelMapper : Mapper<RecipeCardUiModel, RecipeDomain> {

    override fun convert(input: RecipeDomain): RecipeCardUiModel {
        return RecipeCardUiModel(
            id = input.id,
            name = input.title,
            imageUrl = input.imageUrl,
            prepTimeMinute = input.prepTime.formatDuration(),
            isFavorite = input.collections.any { it.systemType == CollectionSystemType.Favorites }
        )
    }
}

// =================================================================================================
// Extension
// =================================================================================================

fun List<RecipeDomain>.toRecipeCardItemUiModels(): ImmutableList<RecipeCardUiModel> {
    return RecipeCardUiModelMapper().convert(this).toImmutableList()
}
