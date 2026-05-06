package com.jmabilon.chefmate.feature.collection.details.sheet.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCase
import com.jmabilon.chefmate.feature.collection.details.sheet.rename.model.RenameCollectionAction
import com.jmabilon.chefmate.feature.collection.details.sheet.rename.model.RenameCollectionEvent
import com.jmabilon.chefmate.feature.collection.details.sheet.rename.model.RenameCollectionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RenameCollectionViewModel(
    private val observeCollectionByIdUseCase: ObserveCollectionByIdUseCase,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    // =================================================================================================
    // Private Properties
    // =================================================================================================

    private var job: Job? = null

    private val _event = Channel<RenameCollectionEvent>()

    private val _state = MutableStateFlow(RenameCollectionState())

    // =================================================================================================
    // Public Properties
    // =================================================================================================

    val event = _event.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = RenameCollectionState()
        )

    // =================================================================================================
    // Public Methods
    // =================================================================================================

    fun onAction(action: RenameCollectionAction) {
        when (action) {
            is RenameCollectionAction.OnCollectionNameChange -> {
                _state.value = _state.value.copy(
                    collectionName = action.name
                )
            }

            is RenameCollectionAction.OnSheetStarted -> observeCollection(action.collectionId)
            RenameCollectionAction.OnRenameCollectionClick -> renameCollection()
        }
    }

    // =================================================================================================
    // Private Methods
    // =================================================================================================

    private fun observeCollection(collectionId: String) {
        job?.cancel()
        job = viewModelScope.launch {
            observeCollectionByIdUseCase(collectionId)
                .collect { collection ->
                    _state.update { currentState ->
                        currentState.copy(
                            collectionId = collection.id,
                            collectionName = collection.name
                        )
                    }
                }
        }
    }

    private fun renameCollection() {
        viewModelScope.launch {
            collectionRepository.renameCollection(
                collectionId = state.value.collectionId,
                newName = state.value.collectionName
            )
                .onSuccess {
                    _event.send(RenameCollectionEvent.CollectionSuccessfullyRenamed)
                }
        }
    }
}
