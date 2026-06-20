package com.jmabilon.chefmate.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.feature.home.domain.usecase.GetHomeSortedCollectionsUseCase
import com.jmabilon.chefmate.feature.home.presentation.mapper.toUiData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    getHomeSortedCollectionsUseCase: GetHomeSortedCollectionsUseCase,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<HomeEvent>()

    private val _collections = getHomeSortedCollectionsUseCase.invoke()
        .map { it.toUiData() }

    private val _dialogState = MutableStateFlow<HomeDialogState?>(null)

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _collections.combine(_dialogState) { collections, dialogState ->
        HomeState(
            collections = collections,
            dialogState = dialogState
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = HomeState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnDismissDialog -> {
                _dialogState.update { null }
            }

            HomeAction.OnNewCollectionClick -> {
                _dialogState.update { HomeDialogState.CreateCollection }
            }

            is HomeAction.OnCreateCollection -> {
                createCollection(action.collectionName)
            }

            HomeAction.OnScanRecipeClick -> {
                _dialogState.update { HomeDialogState.ScanRecipe }
            }
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun createCollection(collectionName: String) {
        viewModelScope.launch {
            collectionRepository.createCollection(collectionName = collectionName)
                .onFailure { error ->
                    print("Error creating collection: ${error.message}")
                }
        }
    }
}
