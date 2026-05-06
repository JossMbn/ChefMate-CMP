package com.jmabilon.chefmate.feature.collection.selection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionsUseCase
import com.jmabilon.chefmate.domain.collection.usecase.UpdateRecipeCollectionsUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeById
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionAction
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionEvent
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionState
import com.jmabilon.chefmate.feature.collection.selection.model.ui.toCollectionSelectionUiModel
import com.jmabilon.chefmate.feature.collection.selection.navigation.CollectionSelectionRoute
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionSelectionViewModel(
    savedStateHandle: SavedStateHandle,
    observeCollectionsUseCase: ObserveCollectionsUseCase,
    private val observeRecipeById: ObserveRecipeById,
    private val updateRecipeCollectionsUseCase: UpdateRecipeCollectionsUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<CollectionSelectionRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<CollectionSelectionEvent>()

    private val _collections = observeCollectionsUseCase()
        .map { collections ->
            collections.filter { it.systemType == null }
        }
    private val _selectedCollectionIds = MutableStateFlow<List<String>>(emptyList())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _collections.combine(_selectedCollectionIds) { collections, selectedIds ->
        val collections = collections
            .toCollectionSelectionUiModel(selectedCollectionIds = selectedIds)
            .toImmutableList()

        CollectionSelectionState(
            collections = collections,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CollectionSelectionState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    init {
        observeRecipe()
    }

    fun onAction(action: CollectionSelectionAction) {
        when (action) {
            is CollectionSelectionAction.OnCollectionClicked -> {
                toggleCollectionSelection(collectionId = action.collectionId)
            }

            CollectionSelectionAction.OnConfirmClick -> updateRecipeCollections()
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun observeRecipe() {
        viewModelScope.launch {
            observeRecipeById(recipeId = args.recipeId)
                .map { it.collections }
                .collect { collections ->
                    val initialRecipeCollections = collections.filter { it.systemType == null }
                    val initialSelectedCollectionIds = initialRecipeCollections.map { it.id }

                    _selectedCollectionIds.update { initialSelectedCollectionIds }
                }
        }
    }

    private fun toggleCollectionSelection(collectionId: String) {
        val currentlySelectedIds = _selectedCollectionIds.value.toMutableList()

        if (currentlySelectedIds.contains(collectionId)) {
            currentlySelectedIds.remove(collectionId)
        } else {
            currentlySelectedIds.add(collectionId)
        }

        _selectedCollectionIds.update { currentlySelectedIds }
    }

    private fun updateRecipeCollections() {
        viewModelScope.launch {
            updateRecipeCollectionsUseCase(
                recipeId = args.recipeId,
                collectionIds = _selectedCollectionIds.value
            )
                .onSuccess {
                    _event.send(CollectionSelectionEvent.OnUpdateRecipeCollectionsSuccess)
                }
        }
    }
}
