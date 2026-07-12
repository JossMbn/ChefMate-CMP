package com.jmabilon.chefmate.core.designsystem.newcomponent.recipe.model

import com.jmabilon.chefmate.core.presentation.UiText

data class RecipeCardUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val prepTimeMinute: UiText?,
    val isFavorite: Boolean
)
