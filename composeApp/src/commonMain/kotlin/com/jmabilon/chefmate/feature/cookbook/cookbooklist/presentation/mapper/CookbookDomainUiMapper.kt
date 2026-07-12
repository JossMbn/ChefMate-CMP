package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.mapper

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_uncategorized_display_name
import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.toAsyncState
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.model.CookbookUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class CookbookDomainUiMapper : Mapper<CookbookUiModel, CookbookDomain> {

    override fun convert(input: CookbookDomain): CookbookUiModel {
        val imageUrls = input.recipes.take(3).mapNotNull { it.imageUrl }

        return CookbookUiModel(
            id = input.id,
            name = input.getDisplayName(),
            imageUrls = imageUrls.toImmutableList(),
            recipeCount = input.recipeCount
        )
    }

    private fun CookbookDomain.getDisplayName(): UiText {
        if (systemType == CookbookSystemType.Favorites) return UiText.Empty

        return when (systemType) {
            CookbookSystemType.Uncategorized -> UiText.ResourceString(Res.string.recipe_uncategorized_display_name)
            null -> UiText.DynamicString(name)
        }
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<CookbookDomain>.toAsyncUiModel(): AsyncState<ImmutableList<CookbookUiModel>> {
    return CookbookDomainUiMapper().convertOrEmpty(this).toImmutableList().toAsyncState()
}
