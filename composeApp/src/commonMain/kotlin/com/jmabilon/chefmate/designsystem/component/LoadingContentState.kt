package com.jmabilon.chefmate.designsystem.component

import androidx.compose.runtime.Stable

@Stable
sealed interface LoadingContentState {

    data object Loading : LoadingContentState

    data object Content : LoadingContentState

    data object NoResults : LoadingContentState

    data object Error : LoadingContentState
}
