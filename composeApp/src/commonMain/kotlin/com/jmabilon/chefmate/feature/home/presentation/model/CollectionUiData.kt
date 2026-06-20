package com.jmabilon.chefmate.feature.home.presentation.model

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_favorites_display_name
import chefmate.composeapp.generated.resources.recipe_uncategorized_display_name
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import kotlinx.collections.immutable.ImmutableList

data class CollectionUiData(
    val id: String,
    val name: String,
    val imageUrls: ImmutableList<String>,
    val recipeCount: Int,
    val systemType: CollectionSystemType?
) {

    val displayName: UiText
        get() = when (systemType) {
            CollectionSystemType.Uncategorized -> UiText.ResourceString(Res.string.recipe_uncategorized_display_name)
            CollectionSystemType.Favorites -> UiText.ResourceString(Res.string.recipe_favorites_display_name)
            null -> UiText.DynamicString(name)
        }
}
