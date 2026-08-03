package com.jmabilon.chefmate.feature.recipe.creation2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.stateholder.RecipeEditorStateHolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class RecipeEditorViewModel(
    private val recipeEditorStateHolder: RecipeEditorStateHolder
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<RecipeEditorEvent>()

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = recipeEditorStateHolder.recipe
        .map { recipe ->
            RecipeEditorState(
                recipe = recipe
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = RecipeEditorState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: RecipeEditorAction) {
        when (action) {
            is RecipeEditorAction.OnImagePicked -> recipeEditorStateHolder.onImageChanged(action.bytes)
            is RecipeEditorAction.OnTitleChanged -> recipeEditorStateHolder.onTitleChanged(action.title)
            is RecipeEditorAction.OnPrepTimeChanged -> recipeEditorStateHolder.onPrepTimeChanged(
                action.hour,
                action.minute
            )

            is RecipeEditorAction.OnCookTimeChanged -> recipeEditorStateHolder.onCookTimeChanged(
                action.hour,
                action.minute
            )

            RecipeEditorAction.OnDecreaseServesClicked -> recipeEditorStateHolder.onDecreaseServes()
            RecipeEditorAction.OnIncreaseServesClicked -> recipeEditorStateHolder.onIncreaseServes()

            is RecipeEditorAction.OnDifficultyChanged -> recipeEditorStateHolder.onDifficultyChanged(
                action.difficulty
            )

        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================
}
