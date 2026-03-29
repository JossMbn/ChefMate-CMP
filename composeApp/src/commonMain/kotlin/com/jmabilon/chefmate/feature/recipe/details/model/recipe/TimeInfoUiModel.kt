package com.jmabilon.chefmate.feature.recipe.details.model.recipe

import com.jmabilon.chefmate.designsystem.utils.UiText

data class TimeInfoUiModel(
    val prepTimeText: UiText? = null, // "1h 15 min"
    val cookTimeText: UiText? =  null, // "30 min"
)
