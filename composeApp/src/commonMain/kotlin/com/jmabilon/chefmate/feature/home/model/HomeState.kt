package com.jmabilon.chefmate.feature.home.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface HomeDialogState {
    data object CreateCollection : HomeDialogState
}

data class HomeState(
    val collections: ImmutableList<CollectionUiData> = persistentListOf(),
    val dialogState: HomeDialogState? = null
)
