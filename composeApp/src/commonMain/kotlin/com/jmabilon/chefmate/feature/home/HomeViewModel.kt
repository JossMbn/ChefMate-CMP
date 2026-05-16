package com.jmabilon.chefmate.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.domain.collection.usecase.CreateCollectionUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionsUseCase
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import com.jmabilon.chefmate.feature.home.mapper.toUiData
import com.jmabilon.chefmate.feature.home.model.HomeAction
import com.jmabilon.chefmate.feature.home.model.HomeDialogState
import com.jmabilon.chefmate.feature.home.model.HomeEvent
import com.jmabilon.chefmate.feature.home.model.HomeState
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
    observeCollectionsUseCase: ObserveCollectionsUseCase,
    private val createCollectionUseCase: CreateCollectionUseCase
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<HomeEvent>()

    private val _collections = observeCollectionsUseCase()
        .map { collections ->
            collections
                // Uncategorized and Favorites collections should always be at the top of the list, sorted by updatedAt desc
                .sortedWith(
                    compareByDescending<CollectionDomain> { it.systemType == CollectionSystemType.Uncategorized }
                        .thenByDescending { it.systemType == CollectionSystemType.Favorites }
                        .thenByDescending { it.updatedAt }
                )
                .toUiData()
        }

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
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun createCollection(collectionName: String) {
        viewModelScope.launch {
            createCollectionUseCase(collectionName = collectionName)
                .onFailure { error ->
                    print("Error creating collection: ${error.message}")
                }
        }
    }
}
