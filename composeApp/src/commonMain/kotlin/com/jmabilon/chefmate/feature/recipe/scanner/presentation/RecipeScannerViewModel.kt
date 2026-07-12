package com.jmabilon.chefmate.feature.recipe.scanner.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeScannerViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val args = savedStateHandle.toRoute<RecipeScannerRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = Channel<RecipeScannerEvent>()

    private val _state = MutableStateFlow(RecipeScannerState())

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.receiveAsFlow()

    val state = _state
        .onStart {
            loadData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = RecipeScannerState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: RecipeScannerAction) {
        when (action) {
            is RecipeScannerAction.OnImagePick -> scanningImage(image = action.image)
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun loadData() {
        /*when (args.type) {
            RecipeScannerType.ImageScan -> {
                _state.update {
                    it.copy(
                        contentView = RecipeScannerContentView.Initializing,
                        scanningType = args.type
                    )
                }
            }
        }*/
    }

    private fun scanningImage(image: List<Byte>?) {
        viewModelScope.launch {
            if (image.isNullOrEmpty()) return@launch

            _state.update { it.copy(contentView = RecipeScannerContentView.Scanning) }

            recipeRepository.scanRecipeFromImage(imageData = image)
                .onSuccess { recipe ->
                    val test = recipe
                    // TODO: Handle scanned recipe
                }
                .onFailure { error ->
                    val test = error
                }
        }
    }
}
