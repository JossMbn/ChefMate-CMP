package com.jmabilon.chefmate.feature.cookbook.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.core.presentation.mapper.recipe.toAsyncUiModels
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

class CookbookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    observeCookbookByIdUseCase: ObserveCookbookByIdUseCase,
    private val cookbookRepository: CookbookRepository
) : ViewModel() {

    // =============================================================================================
    // Arguments
    // =============================================================================================

    private val args = savedStateHandle.toRoute<CookbookDetailsRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<CookbookDetailsEvent>()

    private val _internalState = MutableStateFlow(CookbookDetailsState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = observeCookbookByIdUseCase(cookbookId = args.cookbookId)
        .combine(_internalState) { details, internalState ->
            CookbookDetailsState(
                cookbookTitle = details.name,
                isSystemCookbook = details.systemType != null,
                recipes = details.recipes.toAsyncUiModels(),
                dialogState = internalState.dialogState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = CookbookDetailsState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: CookbookDetailsAction) {
        when (action) {
            CookbookDetailsAction.OnRenameCookbookClick -> {
                _internalState.update {
                    it.copy(
                        dialogState = CookbookDetailsDialogState.RenameCookbook(
                            cookbookId = args.cookbookId
                        )
                    )
                }
            }

            CookbookDetailsAction.OnDeleteCookbookClick -> {
                deleteCookbook()
            }

            CookbookDetailsAction.OnDialogDismiss -> {
                _internalState.update {
                    it.copy(
                        dialogState = null
                    )
                }
            }

            is CookbookDetailsAction.OnFavoriteRecipeClick -> {
                handleFavoriteRecipe(recipeId = action.recipeId)
            }

            CookbookDetailsAction.OnAddRecipeClick -> {
                _internalState.update {
                    it.copy(
                        dialogState = CookbookDetailsDialogState.AddRecipe
                    )
                }
            }
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun deleteCookbook() {
        viewModelScope.launch {
            cookbookRepository.deleteCookbook(cookbookId = args.cookbookId)
                .onSuccess {
                    _event.send(CookbookDetailsEvent.OnCookbookDeleted)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }

    private fun handleFavoriteRecipe(recipeId: String) {
        viewModelScope.launch {
            cookbookRepository.toggleRecipeToFavoriteCookbook(recipeId = recipeId)
        }
    }
}
