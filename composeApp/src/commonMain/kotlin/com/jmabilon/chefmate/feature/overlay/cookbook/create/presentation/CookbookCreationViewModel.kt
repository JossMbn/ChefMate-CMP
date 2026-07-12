package com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CookbookCreationViewModel(
    private val cookbookRepository: CookbookRepository
) : ViewModel() {

    // =================================================================================================
    // Private properties
    // =================================================================================================

    private val _event = Channel<CookbookCreationEvent>()

    private val _state = MutableStateFlow(CookbookCreationState())

    // =================================================================================================
    // Public properties
    // =================================================================================================

    val event = _event.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = CookbookCreationState()
        )

    // =================================================================================================
    // Public methods
    // =================================================================================================

    fun onAction(action: CookbookCreationAction) {
        when (action) {
            is CookbookCreationAction.OnCookbookNameChange -> {
                _state.update { it.copy(cookbookName = action.name) }
            }

            CookbookCreationAction.OnCreateCookbookClick -> createCookbook()
        }
    }

    // =================================================================================================
    // Private methods
    // =================================================================================================

    private fun createCookbook() {
        viewModelScope.launch {
            val cookbookName = state.value.cookbookName

            cookbookRepository.createCookbook(cookbookName = cookbookName)
                .onSuccess {
                    _state.update { it.copy(cookbookName = "") }
                    _event.send(CookbookCreationEvent.OnCookbookCreated)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }
}
