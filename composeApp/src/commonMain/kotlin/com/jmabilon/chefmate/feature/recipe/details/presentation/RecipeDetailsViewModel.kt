package com.jmabilon.chefmate.feature.recipe.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.core.presentation.isContent
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import com.jmabilon.chefmate.feature.recipe.details.presentation.mapper.toAsyncRecipeUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val cookbookRepository: CookbookRepository
) : ViewModel() {

    // =============================================================================================
    // Arguments
    // =============================================================================================

    private val args = savedStateHandle.toRoute<RecipeDetailsRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<RecipeDetailsEvent>()

    private val _baseRecipe = recipeRepository.observeRecipeById(recipeId = args.recipeId)
        .catch { error ->
            println("Error fetching recipe details: ${error.message}")
        }

    private val _internalState = MutableStateFlow(RecipeInternalState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _baseRecipe.combine(_internalState) { baseRecipe, internalState ->
        RecipeDetailsState(
            recipe = baseRecipe.toAsyncRecipeUiModel(customServings = internalState.servings),
            isInFavorites = baseRecipe.cookbooks.any { it.systemType == CookbookSystemType.Favorites }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = RecipeDetailsState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: RecipeDetailsAction) {
        when (action) {
            is RecipeDetailsAction.OnServingsChanged -> onServingChange(action = action.action)
            RecipeDetailsAction.OnDeleteRecipeClick -> deleteRecipe()
            RecipeDetailsAction.OnFavoriteClick -> toggleRecipeToFavorite()
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun onServingChange(action: RecipeServingActionType) {
        val currentRecipe = state.value.recipe

        if (!currentRecipe.isContent()) return

        val data = currentRecipe.data
        val currentServings = data.serving.toInt()
        val newServings = when (action) {
            RecipeServingActionType.Increment -> currentServings + 1
            RecipeServingActionType.Decrement -> currentServings - 1
        }.coerceAtLeast(1)

        _internalState.update { it.copy(servings = newServings) }
    }

    private fun deleteRecipe() {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipeId = args.recipeId)
                .onSuccess {
                    _event.send(RecipeDetailsEvent.RecipeSuccessfullyDeleted)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }

    private fun toggleRecipeToFavorite() {
        viewModelScope.launch {
            cookbookRepository.toggleRecipeToFavoriteCookbook(recipeId = args.recipeId)
        }
    }
}
