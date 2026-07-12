package com.jmabilon.chefmate.feature.cookbook.selection.presentation.model

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CookbookSelectionUiModel(
    val id: String,
    val imageUrl: String?,
    val name: String,
    val recipeCount: Int,
    val checked: Boolean
)

class CookbookSelectionUiModelMapper(
    val selectedCookbookIds: List<String>
) : Mapper<CookbookSelectionUiModel, CookbookDomain> {

    override fun convert(input: CookbookDomain): CookbookSelectionUiModel {
        val imageUrl = input.recipes.firstOrNull()?.imageUrl

        return CookbookSelectionUiModel(
            id = input.id,
            imageUrl = imageUrl,
            name = input.name,
            recipeCount = input.recipeCount,
            checked = selectedCookbookIds.contains(input.id)
        )
    }
}

fun List<CookbookDomain>.toCookbookSelectionUiModel(
    selectedCookbookIds: List<String>
): ImmutableList<CookbookSelectionUiModel> {
    return CookbookSelectionUiModelMapper(selectedCookbookIds = selectedCookbookIds)
        .convert(this)
        .toImmutableList()
}
