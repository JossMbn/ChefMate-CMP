package com.jmabilon.chefmate.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.feature.home.domain.usecase.GetHomeScreenDataUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(
    getHomeScreenDataUseCase: GetHomeScreenDataUseCase
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<HomeEvent>()

    private val _internalState = MutableStateFlow(HomeState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = getHomeScreenDataUseCase.invoke()
        .combine(_internalState) { screenData, internalState ->
            HomeState(
                favoriteCookbookId = screenData.favoriteCookbookId,
                dialogState = internalState.dialogState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = HomeState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnDismissDialog -> {
                _internalState.update { it.copy(dialogState = null) }
            }

            HomeAction.OnScanRecipeClick -> {
                _internalState.update { it.copy(dialogState = HomeDialogState.ScanRecipe) }
            }
        }
    }
}
