package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.model

import com.jmabilon.chefmate.core.presentation.UiText
import kotlinx.collections.immutable.ImmutableList

data class CookbookUiModel(
    val id: String,
    val name: UiText,
    val imageUrls: ImmutableList<String>,
    val recipeCount: Int
)
