package com.jmabilon.chefmate.feature.recipe.details.presentation.model

import com.jmabilon.chefmate.core.presentation.UiText

data class TimeInfoUiModel(
    val prepTimeText: UiText? = null, // "1h 15 min"
    val cookTimeText: UiText? =  null, // "30 min"
)
