package com.jmabilon.chefmate.feature.cookbook.details.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.core.designsystem.newcomponent.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.core.presentation.AsyncState
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface CookbookDetailsDialogState {
    data class RenameCookbook(val cookbookId: String) : CookbookDetailsDialogState
    data object AddRecipe : CookbookDetailsDialogState
}

data class CookbookDetailsState(
    val cookbookTitle: String = "",
    val isSystemCookbook: Boolean = false,
    val recipes: AsyncState<ImmutableList<RecipeCardUiModel>> = AsyncState.Loading,
    val dialogState: CookbookDetailsDialogState? = null
)
