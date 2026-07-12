package com.jmabilon.chefmate.core.presentation.mapper.recipe

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.designsystem.newcomponent.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.core.presentation.toAsyncState
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class RecipeCardUiModelMapper : Mapper<RecipeCardUiModel, RecipeDomain> {

    override fun convert(input: RecipeDomain): RecipeCardUiModel {
        return RecipeCardUiModel(
            id = input.id,
            name = input.title,
            imageUrl = input.imageUrl,
            prepTimeMinute = input.prepTime.formatDuration(),
            isFavorite = input.cookbooks.any { it.systemType == CookbookSystemType.Favorites }
        )
    }
}

// =================================================================================================
// Extension
// =================================================================================================

fun List<RecipeDomain>.toAsyncUiModels(): AsyncState<ImmutableList<RecipeCardUiModel>> {
    return RecipeCardUiModelMapper().convert(this).toImmutableList().toAsyncState()
}
