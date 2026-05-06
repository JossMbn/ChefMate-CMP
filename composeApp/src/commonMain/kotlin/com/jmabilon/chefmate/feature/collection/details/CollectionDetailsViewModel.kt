package com.jmabilon.chefmate.feature.collection.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.designsystem.component.recipe.model.toRecipeCardItemUiModels
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.collection.usecase.DeleteCollectionUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCase
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsAction
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsDialogState
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsDialogState.RenameCollection
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsEvent
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsState
import com.jmabilon.chefmate.feature.collection.details.navigation.CollectionDetailsRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    observeCollectionByIdUseCase: ObserveCollectionByIdUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
    private val collectionRepository: CollectionRepository
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

    private val _dialogState = MutableStateFlow<CollectionDetailsDialogState?>(null)

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _collection.combine(_dialogState) { details, dialogState ->
        CollectionDetailsState(
            loadingContentState = LoadingContentState.Content,
            collectionTitle = details.name,
            systemType = details.systemType,
            recipes = details.recipes.toRecipeCardItemUiModels(),
            dialogState = dialogState
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
            CollectionDetailsAction.OnRenameCollectionClick -> {
                _dialogState.update {
                    RenameCollection(
                        collectionId = args.collectionId
                    )
                }
            }

            CollectionDetailsAction.OnDeleteCollectionClick -> deleteCollection()

            CollectionDetailsAction.OnDialogDismiss -> _dialogState.update { null }

            is CollectionDetailsAction.OnFavoriteRecipeClick -> handleFavoriteRecipe(recipeId = action.recipeId)
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

    private fun handleFavoriteRecipe(recipeId: String) {
        viewModelScope.launch {
            collectionRepository.toggleRecipeToFavoriteCollection(recipeId = recipeId)
        }
    }
}
