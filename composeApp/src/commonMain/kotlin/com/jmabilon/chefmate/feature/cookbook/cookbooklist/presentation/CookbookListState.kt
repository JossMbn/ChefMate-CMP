package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.model.CookbookUiModel
import kotlinx.collections.immutable.ImmutableList

data class CookbookListState(
    val cookbooks: AsyncState<ImmutableList<CookbookUiModel>> = AsyncState.Loading,
    val dialogState: CookbookListDialogState? = null
)

@Stable
sealed interface CookbookListDialogState {
    data object CreateCookbook : CookbookListDialogState
}
