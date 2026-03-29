package com.jmabilon.chefmate.feature.collection.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.designsystem.component.recipe.model.toRecipeCardItemUiModels
import com.jmabilon.chefmate.domain.collection.usecase.DeleteCollectionUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCase
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsAction
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsEvent
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsState
import com.jmabilon.chefmate.feature.collection.details.navigation.CollectionDetailsRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollectionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    observeCollectionByIdUseCase: ObserveCollectionByIdUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase
) : ViewModel() {

    // =============================================================================================
    // Arguments
    // =============================================================================================

    private val args = savedStateHandle.toRoute<CollectionDetailsRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<CollectionDetailsEvent>()

    private val _collection = observeCollectionByIdUseCase(collectionId = args.collectionId)

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _collection
        .map { details ->
            CollectionDetailsState(
                loadingContentState = LoadingContentState.Content,
                collectionTitle = details.name,
                systemType = details.systemType,
                recipes = details.recipes.toRecipeCardItemUiModels()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CollectionDetailsState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: CollectionDetailsAction) {
        when (action) {
            CollectionDetailsAction.OnDeleteCollectionClick -> deleteCollection()
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun deleteCollection() {
        viewModelScope.launch {
            deleteCollectionUseCase(collectionId = args.collectionId)
                .onSuccess {
                    _event.send(CollectionDetailsEvent.OnCollectionDeleted)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }
}
