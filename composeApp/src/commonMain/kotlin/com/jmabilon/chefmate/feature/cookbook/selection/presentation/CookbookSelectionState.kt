package com.jmabilon.chefmate.feature.cookbook.selection.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.model.CookbookSelectionUiModel
import kotlinx.collections.immutable.ImmutableList

data class CookbookSelectionState(
    val cookbooks: AsyncState<ImmutableList<CookbookSelectionUiModel>> = AsyncState.Loading,
    val dialogState: CookbookSelectionDialogState? = null
)

@Stable
sealed interface CookbookSelectionDialogState {
    data object CreateCookbook : CookbookSelectionDialogState
}
