package com.jmabilon.chefmate.feature.home.presentation

import androidx.compose.runtime.Stable

@Stable
sealed interface HomeDialogState {
    data object ScanRecipe : HomeDialogState
}

data class HomeState(
    val favoriteCookbookId: String = "",
    val dialogState: HomeDialogState? = null
)
