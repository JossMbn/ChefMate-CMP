package com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.cookbook.usecase.ObserveCookbookByIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class RenameCookbookViewModel(
    private val cookbookId: String,
    observeCookbookByIdUseCase: ObserveCookbookByIdUseCase,
    private val cookbookRepository: CookbookRepository
) : ViewModel() {

    // =================================================================================================
    // Private Properties
    // =================================================================================================

    private val _event = Channel<RenameCookbookEvent>()

    private val _internalState = MutableStateFlow(RenameCookbookInternalState())

    // =================================================================================================
    // Public Properties
    // =================================================================================================

    val event = _event.receiveAsFlow()

    val state = observeCookbookByIdUseCase(cookbookId)
        .combine(_internalState) { cookbook, internalState ->
            RenameCookbookState(
                cookbookName = internalState.cookbookName ?: cookbook.name
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = RenameCookbookState()
        )

    // =================================================================================================
    // Public Methods
    // =================================================================================================

    fun onAction(action: RenameCookbookAction) {
        when (action) {
            is RenameCookbookAction.OnCookbookNameChange -> {
                _internalState.update { it.copy(cookbookName = action.name) }
            }

            RenameCookbookAction.OnRenameCookbookClick -> renameCookbook()
        }
    }

    // =================================================================================================
    // Private Methods
    // =================================================================================================

    private fun renameCookbook() {
        viewModelScope.launch {
            cookbookRepository.renameCookbook(
                cookbookId = cookbookId,
                newName = state.value.cookbookName
            )
                .onSuccess {
                    _event.send(RenameCookbookEvent.CookbookSuccessfullyRenamed)
                }
        }
    }
}
