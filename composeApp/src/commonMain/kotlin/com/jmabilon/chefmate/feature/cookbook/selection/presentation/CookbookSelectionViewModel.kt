package com.jmabilon.chefmate.feature.cookbook.selection.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.core.presentation.toAsyncState
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.model.toCookbookSelectionUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CookbookSelectionViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val cookbookRepository: CookbookRepository
) : ViewModel() {

    private val args = savedStateHandle.toRoute<CookbookSelectionRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<CookbookSelectionEvent>()

    private val _cookbooks = cookbookRepository.observeCookbooks()
        .map { cookbooks ->
            cookbooks
                .filter { it.systemType == null }
                .sortedBy { it.name }
        }
        .catch { emit(emptyList()) }

    private val _selectedCookbookIds = MutableStateFlow<List<String>>(emptyList())

    private val _initialSelectedCookbookIds = MutableStateFlow<List<String>>(emptyList())

    private val _internalState = MutableStateFlow(CookbookSelectionState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state =
        combine(
            _cookbooks,
            _selectedCookbookIds,
            _internalState
        ) { cookbooks, selectedIds, internalState ->
            val cookbooks = cookbooks
                .toCookbookSelectionUiModel(selectedCookbookIds = selectedIds)
                .toAsyncState()

            CookbookSelectionState(
                cookbooks = cookbooks,
                dialogState = internalState.dialogState
            )
        }
            .onStart {
                observeRecipe()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds),
                initialValue = CookbookSelectionState()
            )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: CookbookSelectionAction) {
        when (action) {
            is CookbookSelectionAction.OnCookbookClicked -> {
                toggleCookbookSelection(cookbookId = action.cookbookId)
            }

            CookbookSelectionAction.OnConfirmClicked -> updateRecipeCookbooks()

            CookbookSelectionAction.OnCookbookSelectionClicked -> {
                _internalState.update { it.copy(dialogState = CookbookSelectionDialogState.CreateCookbook) }
            }

            CookbookSelectionAction.OnDismissDialog -> {
                _internalState.update { it.copy(dialogState = null) }
            }
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private var observeRecipeJob: Job? = null

    private fun observeRecipe() {
        observeRecipeJob?.cancel()
        observeRecipeJob = viewModelScope.launch {
            recipeRepository.observeRecipeById(recipeId = args.recipeId)
                .collect { recipe ->
                    val initialSelectedRecipeCookbookIds = recipe.cookbooks
                        .filter { it.systemType == null }
                        .map { it.id }

                    _initialSelectedCookbookIds.update { initialSelectedRecipeCookbookIds }
                    _selectedCookbookIds.update { initialSelectedRecipeCookbookIds }
                }
        }
    }

    private fun toggleCookbookSelection(cookbookId: String) {
        val currentlySelectedIds = _selectedCookbookIds.value.toMutableList()

        if (currentlySelectedIds.contains(cookbookId)) {
            currentlySelectedIds.remove(cookbookId)
        } else {
            currentlySelectedIds.add(cookbookId)
        }

        _selectedCookbookIds.update { currentlySelectedIds }
    }

    private fun updateRecipeCookbooks() {
        viewModelScope.launch {
            val initialCookbookIds = _initialSelectedCookbookIds.value
            val cookbookIds = _selectedCookbookIds.value

            if (cookbookIds == initialCookbookIds) {
                _event.send(CookbookSelectionEvent.OnUpdateRecipeCookbooksSuccess)
                return@launch
            }

            cookbookRepository.updateRecipeCookbooks(
                recipeId = args.recipeId,
                cookbookIds = _selectedCookbookIds.value
            )
                .onSuccess {
                    _event.send(CookbookSelectionEvent.OnUpdateRecipeCookbooksSuccess)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }
}
