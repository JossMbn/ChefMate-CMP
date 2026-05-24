package com.jmabilon.chefmate.core.designsystem.component

import androidx.compose.runtime.Stable

@Stable
sealed interface LoadingContentState {

    data object Loading : LoadingContentState

    data object Content : LoadingContentState

    data object NoResults : LoadingContentState

    data object Error : LoadingContentState
}
