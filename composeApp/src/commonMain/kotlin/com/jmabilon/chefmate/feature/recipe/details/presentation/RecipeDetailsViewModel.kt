package com.jmabilon.chefmate.feature.recipe.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import com.jmabilon.chefmate.feature.recipe.details.presentation.mapper.toRecipeDetailsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionUiModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    // =============================================================================================
    // Arguments
    // =============================================================================================

    private val args = savedStateHandle.toRoute<RecipeDetailsRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<RecipeDetailsEvent>()

    private val _state = MutableStateFlow(RecipeDetailsState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _state
        .onStart {
            getRecipeDetails()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipeDetailsState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: RecipeDetailsAction) {
        when (action) {
            is RecipeDetailsAction.OnServingsChanged -> onServingChange(action = action.action)
            RecipeDetailsAction.OnDeleteRecipeClick -> deleteRecipe()
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun getRecipeDetails() {
        viewModelScope.launch {
            recipeRepository.observeRecipeById(recipeId = args.recipeId)
                .catch { error ->
                    _state.update { it.copy(loadingContentState = LoadingContentState.Error) }
                    println("Error fetching recipe details: ${error.message}")
                }
                .collect { recipe ->
                    _state.update {
                        it.copy(
                            loadingContentState = LoadingContentState.Content,
                            recipeDetails = recipe.toRecipeDetailsUiModel()
                        )
                    }
                }
        }
    }

    private fun onServingChange(action: RecipeServingActionUiModel) {
        val currentRecipeDetails = state.value.recipeDetails
        val currentServings = currentRecipeDetails.ingredients.servings

        val newServings = when (action) {
            RecipeServingActionUiModel.Increment -> currentServings + 1
            RecipeServingActionUiModel.Decrement -> currentServings - 1
        }.coerceAtLeast(1)

        val recalculateIngredients = recalculateIngredientsQuantity(
            baseIngredients = currentRecipeDetails.ingredients,
            newServings = newServings
        )

        _state.update {
            it.copy(
                recipeDetails = currentRecipeDetails.copy(
                    ingredients = recalculateIngredients
                )
            )
        }
    }

    private fun recalculateIngredientsQuantity(
        baseIngredients: IngredientsUiModel,
        newServings: Int
    ): IngredientsUiModel {
        val currentServings = baseIngredients.servings

        val newGroups = baseIngredients.groups.map { group ->
            group.copy(
                items = group.items.map { item ->
                    item.copy(
                        currentQuantity = item.currentQuantity?.let {
                            (it / currentServings) * newServings
                        } ?: item.currentQuantity
                    )
                }.toImmutableList()
            )
        }.toImmutableList()

        return baseIngredients.copy(
            servings = newServings,
            groups = newGroups
        )
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
}
