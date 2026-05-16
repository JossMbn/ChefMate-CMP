package com.jmabilon.chefmate.feature.recipe.creation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.recipe.usecase.CreateOrUpdateRecipeWithImageUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeByIdUseCase
import com.jmabilon.chefmate.feature.recipe.creation.mapper.toDomain
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationContext
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationDialogState
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationEvent
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationState
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.ImageSource
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationIngredientSectionUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationIngredientUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationInstructionUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeTimeCreationUiModel
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.toRecipeCreationUiModel
import com.jmabilon.chefmate.feature.recipe.creation.navigation.ManualRecipeCreationRoute
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ManualRecipeCreationViewModel(
    savedStateHandle: SavedStateHandle,
    private val createOrUpdateRecipeWithImageUseCase: CreateOrUpdateRecipeWithImageUseCase,
    private val observeRecipeByIdUseCase: ObserveRecipeByIdUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<ManualRecipeCreationRoute>()

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = MutableSharedFlow<ManualRecipeCreationEvent>()

    private val _context = MutableStateFlow(
        when {
            args.recipeId != null -> ManualRecipeCreationContext.Edition
            else -> ManualRecipeCreationContext.Creation
        }
    )

    private val _recipe = MutableStateFlow(RecipeCreationUiModel())

    private val _dialogState = MutableStateFlow<ManualRecipeCreationDialogState?>(null)

    private val _isCreatingOrUpdatingRecipe = MutableStateFlow(false)

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.asSharedFlow()

    val state = combine(
        _context,
        _isCreatingOrUpdatingRecipe,
        _recipe,
        _dialogState
    ) { context, isCreatingRecipe, recipe, dialogState ->
        ManualRecipeCreationState(
            context = context,
            isCreatingRecipe = isCreatingRecipe,
            recipe = recipe,
            dialogState = dialogState
        )
    }.onStart {
        getInitialData()
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = ManualRecipeCreationState()
    )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: ManualRecipeCreationAction) {
        when (action) {
            // Recipe info actions
            is ManualRecipeCreationAction.OnTitleChange -> onRecipeInfoChange(newTitle = action.newTitle)
            is ManualRecipeCreationAction.OnImageChange -> {
                onRecipeImageChange(newImageBytes = action.newImage)
            }

            is ManualRecipeCreationAction.OnPrepTimeChange -> onPrepTimeChange(
                hour = action.newPrepTimeHour, minute = action.newPrepTimeMinute
            )

            is ManualRecipeCreationAction.OnCookTimeChange -> onCookTimeChange(
                hour = action.newCookTimeHour, minute = action.newCookTimeMinute
            )

            is ManualRecipeCreationAction.OnServingsChange -> {
                onRecipeInfoChange(newServings = action.newServings)
            }

            is ManualRecipeCreationAction.OnDifficultyChange -> {
                onRecipeInfoChange(newDifficulty = action.newDifficulty)
            }

            is ManualRecipeCreationAction.OnSourceUrlChange -> {
                onRecipeInfoChange(newSourceUrl = action.newSourceUrl)
            }

            // Ingredients sections actions
            is ManualRecipeCreationAction.OnCreateIngredientSection -> createIngredientSection(
                newSectionName = action.newSectionName
            )

            is ManualRecipeCreationAction.OnRenameIngredientSectionName -> renameIngredientSection(
                sectionId = action.sectionId, newName = action.newSectionName
            )

            is ManualRecipeCreationAction.OnRemoveIngredientSectionClick -> deleteIngredientSection(
                sectionId = action.sectionId
            )

            // Ingredient actions
            is ManualRecipeCreationAction.OnCreateOrEditMainIngredient -> createOrEditMainIngredient(
                ingredientId = action.ingredientId,
                name = action.name,
                quantity = action.quantity,
                unit = action.unit,
                note = action.note
            )

            is ManualRecipeCreationAction.OnAddSectionIngredient -> createOrEditSectionIngredient(
                sectionId = action.sectionId,
                name = action.name,
                quantity = action.quantity,
                unit = action.unit,
                note = action.note
            )

            is ManualRecipeCreationAction.OnEditSectionIngredient -> createOrEditSectionIngredient(
                sectionId = action.sectionId,
                ingredientId = action.ingredientId,
                name = action.name,
                quantity = action.quantity,
                unit = action.unit,
                note = action.note
            )


            is ManualRecipeCreationAction.OnRemoveIngredient -> removeIngredient(
                ingredientId = action.ingredientId,
                sectionId = action.sectionId
            )

            // Dialog actions
            ManualRecipeCreationAction.OnDismissDialog -> onDismissDialog()
            is ManualRecipeCreationAction.OnShowCreateOrEditIngredientSectionNameDialog -> onShowCreateOrEditIngredientSectionNameDialog(
                sectionId = action.sectionId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditMainIngredientDialog -> onShowCreateOrEditMainIngredientDialog(
                ingredientId = action.ingredientId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditSectionIngredientDialog -> onShowCreateOrEditSectionIngredientDialog(
                sectionId = action.sectionId, ingredientId = action.ingredientId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditInstructionDialog -> onShowCreateOrEditInstructionDialog(
                instructionId = action.instructionId
            )

            is ManualRecipeCreationAction.OnCreateOrEditInstruction -> createOrEditInstruction(
                instructionId = action.instructionId, title = action.title, instruction = action.instruction
            )

            is ManualRecipeCreationAction.OnRemoveInstruction -> deleteInstruction(
                instructionId = action.instructionId
            )

            ManualRecipeCreationAction.OnCreateRecipeClick -> createRecipe()
        }
    }

    // =============================================================================================
    // Private Methods
    // =============================================================================================

    private fun getInitialData() {
        viewModelScope.launch {
            if (args.recipeId == null) return@launch

            observeRecipeByIdUseCase(recipeId = args.recipeId).collect { recipe ->
                _recipe.update { recipe.toRecipeCreationUiModel(emptyList()) }
            }
        }
    }

    private fun createRecipe() {
        viewModelScope.launch {
            val mappedRecipe = state.value.recipe.toDomain()
            val imageSource = state.value.recipe.imageSource

            _isCreatingOrUpdatingRecipe.emit(true)
            createOrUpdateRecipeWithImageUseCase(recipe = mappedRecipe, imageSource = imageSource)
                .onSuccess {
                    _event.emit(ManualRecipeCreationEvent.RecipeSuccessfullyCreatedOrUpdated)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
                .also {
                    _isCreatingOrUpdatingRecipe.emit(false)
                }
        }
    }

    private fun onPrepTimeChange(
        hour: Int, minute: Int
    ) {
        _recipe.update { currentState ->
            currentState.copy(
                prepTime = RecipeTimeCreationUiModel(
                    hour = hour, minute = minute
                )
            )
        }
    }

    private fun onCookTimeChange(
        hour: Int, minute: Int
    ) {
        _recipe.update { currentState ->
            currentState.copy(
                cookTime = RecipeTimeCreationUiModel(
                    hour = hour, minute = minute
                )
            )
        }
    }

    // Recipe info methods

    private fun onRecipeInfoChange(
        newTitle: String = _recipe.value.title,
        newServings: String = _recipe.value.servings,
        newDifficulty: Int? = _recipe.value.difficulty,
        newSourceUrl: String = _recipe.value.sourceUrl
    ) {
        _recipe.update { currentState ->
            currentState.copy(
                title = newTitle,
                servings = newServings,
                difficulty = newDifficulty,
                sourceUrl = newSourceUrl
            )
        }
    }

    private fun onRecipeImageChange(
        newImageBytes: List<Byte>?
    ) {
        val newImageSource = if (!newImageBytes.isNullOrEmpty()) {
            ImageSource.ByteArray(newImageBytes.toImmutableList())
        } else null

        _recipe.update { currentState ->
            currentState.copy(
                imageSource = newImageSource
            )
        }
    }

    // Ingredients sections methods

    private fun createIngredientSection(newSectionName: String) {
        val oderIndex = _recipe.value.ingredientSections.size
        val newSection = RecipeCreationIngredientSectionUiModel(
            id = Uuid.random().toString(),
            name = newSectionName,
            ingredients = persistentListOf(),
            orderIndex = oderIndex
        )

        _recipe.update { currentState ->
            currentState.copy(
                ingredientSections = (currentState.ingredientSections + newSection).toImmutableList()
            )
        }
    }

    private fun renameIngredientSection(sectionId: String, newName: String) {
        _recipe.update { currentState ->
            val updatedSections = currentState.ingredientSections.map { section ->
                if (section.id == sectionId) {
                    section.copy(name = newName)
                } else {
                    section
                }
            }

            currentState.copy(ingredientSections = updatedSections.toImmutableList())
        }
    }

    private fun deleteIngredientSection(sectionId: String) {
        _recipe.update { currentState ->
            val updatedSections = currentState.ingredientSections.filterNot { it.id == sectionId }

            currentState.copy(ingredientSections = updatedSections.toImmutableList())
        }
    }

    // Ingredients methods

    private fun createOrEditMainIngredient(
        ingredientId: String? = null, name: String, quantity: String, unit: String, note: String
    ) {
        _recipe.update { currentState ->
            val updatedMainIngredients = if (ingredientId == null) {
                // Create new ingredient
                val newIngredient = RecipeCreationIngredientUiModel(
                    id = Uuid.random().toString(),
                    name = name,
                    quantity = quantity,
                    unit = unit,
                    note = note,
                    orderIndex = currentState.mainIngredients.size
                )

                currentState.mainIngredients + newIngredient
            } else {
                // Edit existing ingredient
                currentState.mainIngredients.map { ingredient ->
                    if (ingredient.id != ingredientId) return@map ingredient

                    ingredient.copy(
                        name = name, quantity = quantity, unit = unit, note = note
                    )
                }
            }

            currentState.copy(mainIngredients = updatedMainIngredients.toImmutableList())
        }
    }

    private fun createOrEditSectionIngredient(
        sectionId: String,
        ingredientId: String? = null,
        name: String,
        quantity: String,
        unit: String,
        note: String
    ) {
        _recipe.update { currentState ->
            val updatedSections = currentState.ingredientSections.map { section ->
                if (section.id != sectionId) return@map section

                // If ingredientId is null, we are creating a new ingredient, otherwise,
                // we are editing an existing one
                val updatedIngredients = if (ingredientId != null) {
                    section.ingredients.map { ingredient ->
                        if (ingredient.id != ingredientId) return@map ingredient

                        ingredient.copy(
                            name = name, quantity = quantity, unit = unit, note = note
                        )
                    }
                } else {
                    section.ingredients + RecipeCreationIngredientUiModel(
                        id = Uuid.random().toString(),
                        name = name,
                        quantity = quantity,
                        unit = unit,
                        note = note,
                        orderIndex = section.ingredients.size
                    )
                }

                section.copy(ingredients = updatedIngredients.toImmutableList())
            }

            currentState.copy(ingredientSections = updatedSections.toImmutableList())
        }
    }

    private fun removeIngredient(ingredientId: String, sectionId: String?) {
        _recipe.update { currentState ->
            if (sectionId == null) {
                // We are removing a main ingredient
                val updatedMainIngredients = currentState.mainIngredients.filterNot { it.id == ingredientId }

                currentState.copy(mainIngredients = updatedMainIngredients.toImmutableList())
            } else {
                // We are removing a section ingredient
                val updatedSections = currentState.ingredientSections.map { section ->
                    if (section.id != sectionId) return@map section

                    val updatedIngredients = section.ingredients.filterNot { it.id == ingredientId }

                    section.copy(ingredients = updatedIngredients.toImmutableList())
                }

                currentState.copy(ingredientSections = updatedSections.toImmutableList())
            }
        }
    }

    // Instructions methods

    private fun createOrEditInstruction(
        instructionId: String? = null, title: String, instruction: String
    ) {
        _recipe.update { currentState ->
            val updatedInstructions = if (instructionId == null) {
                // Create new instruction
                val newInstruction = RecipeCreationInstructionUiModel(
                    id = Uuid.random().toString(),
                    title = title,
                    instruction = instruction,
                    orderIndex = currentState.instructions.size
                )

                currentState.instructions + newInstruction
            } else {
                // Edit existing instruction
                currentState.instructions.map { currentInstruction ->
                    if (currentInstruction.id != instructionId) return@map currentInstruction

                    currentInstruction.copy(
                        title = title, instruction = instruction
                    )
                }
            }

            currentState.copy(instructions = updatedInstructions.toImmutableList())
        }
    }

    private fun deleteInstruction(instructionId: String) {
        _recipe.update { currentState ->
            val updatedInstructions = currentState.instructions.filterNot { it.id == instructionId }

            currentState.copy(instructions = updatedInstructions.toImmutableList())
        }
    }

    // Dialog methods

    private fun onDismissDialog() {
        _dialogState.update { null }
    }

    private fun onShowCreateOrEditIngredientSectionNameDialog(sectionId: String? = null) {
        val section = sectionId?.let { id ->
            _recipe.value.ingredientSections.firstOrNull { it.id == id } ?: return
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditIngredientSectionName(
                sectionId = sectionId, sectionName = section?.name
            )
        }
    }

    private fun onShowCreateOrEditMainIngredientDialog(ingredientId: String?) {
        val ingredient = ingredientId?.let { id ->
            state.value.recipe.mainIngredients.firstOrNull { it.id == id } ?: return
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditMainIngredient(
                ingredient = ingredient
            )
        }
    }

    private fun onShowCreateOrEditSectionIngredientDialog(
        sectionId: String, ingredientId: String?
    ) {
        val currentSection =
            state.value.recipe.ingredientSections.firstOrNull { it.id == sectionId } ?: return
        val ingredient = ingredientId?.let { id ->
            currentSection.ingredients.firstOrNull { it.id == id }
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditIngredient(
                sectionId = sectionId, sectionName = currentSection.name, ingredient = ingredient
            )
        }
    }

    private fun onShowCreateOrEditInstructionDialog(instructionId: String?) {
        val instruction = instructionId?.let { id ->
            state.value.recipe.instructions.firstOrNull { it.id == id }
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditInstruction(
                instruction = instruction
            )
        }
    }
}
