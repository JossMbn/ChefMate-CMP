package com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.core.presentation.extension.toHourMinute

data class RecipeTimeCreationUiModel(
    val hour: Int? = null,
    val minute: Int? = null,
    val displayText: UiText = UiText.Empty
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeTimeCreationUiModelMapper : Mapper<RecipeTimeCreationUiModel, Int> {

    override fun convert(input: Int): RecipeTimeCreationUiModel {
        val (hour, minute) = input.toHourMinute()
            ?: return RecipeTimeCreationUiModel(displayText = UiText.Empty)

        return RecipeTimeCreationUiModel(
            hour = hour,
            minute = minute,
            displayText = input.formatDuration() ?: UiText.Empty
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun Int.toRecipeTimeCreationUiModel(): RecipeTimeCreationUiModel {
    return RecipeTimeCreationUiModelMapper().convert(this)
}
