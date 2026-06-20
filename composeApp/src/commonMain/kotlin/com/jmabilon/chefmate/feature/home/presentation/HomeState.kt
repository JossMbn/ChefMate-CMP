package com.jmabilon.chefmate.feature.home.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.feature.home.presentation.model.CollectionUiData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface HomeDialogState {
    data object CreateCollection : HomeDialogState
    data object ScanRecipe : HomeDialogState
}

data class HomeState(
    val collections: ImmutableList<CollectionUiData> = persistentListOf(),
    val dialogState: HomeDialogState? = null
)
