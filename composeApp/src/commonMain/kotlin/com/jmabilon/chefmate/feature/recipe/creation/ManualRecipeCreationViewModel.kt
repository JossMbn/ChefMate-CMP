package com.jmabilon.chefmate.feature.recipe.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.recipe.mapper.toDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.domain.recipe.usecase.CreateManualRecipeWithImageUseCase
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationDialogState
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationEvent
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationState
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeInfoUiData
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeIngredientSectionUiData
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeIngredientUiData
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeInstructionUiData
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeUiData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ManualRecipeCreationViewModel(
    private val createManualRecipeWithImageUseCase: CreateManualRecipeWithImageUseCase
) : ViewModel() {

    // =============================================================================================
    // Private Properties
    // =============================================================================================

    private val _event = MutableSharedFlow<ManualRecipeCreationEvent>()

    private val _recipeInfo = MutableStateFlow(RecipeInfoUiData())
    private val _mainIngredients =
        MutableStateFlow<ImmutableList<RecipeIngredientUiData>>(persistentListOf())
    private val _recipeIngredientSections =
        MutableStateFlow<ImmutableList<RecipeIngredientSectionUiData>>(persistentListOf())
    private val _recipeInstructions =
        MutableStateFlow<ImmutableList<RecipeInstructionUiData>>(persistentListOf())

    private val _recipe = combine(
        _recipeInfo,
        _mainIngredients,
        _recipeIngredientSections,
        _recipeInstructions,
        ::RecipeUiData
    )

    private val _dialogState = MutableStateFlow<ManualRecipeCreationDialogState?>(null)

    private val _isCreatingRecipe = MutableStateFlow(false)

    // =============================================================================================
    // Public Properties
    // =============================================================================================

    val event = _event.asSharedFlow()

    val state = combine(
        _isCreatingRecipe,
        _recipe,
        _dialogState
    ) { isCreatingRecipe, recipe, dialogState ->
        ManualRecipeCreationState(
            isCreatingRecipe = isCreatingRecipe,
            recipe = recipe,
            dialogState = dialogState
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ManualRecipeCreationState()
        )

    // =============================================================================================
    // Public Methods
    // =============================================================================================

    fun onAction(action: ManualRecipeCreationAction) {
        when (action) {
            // Recipe info actions
            is ManualRecipeCreationAction.OnTitleChange -> onRecipeInfoChange(newTitle = action.newTitle)
            is ManualRecipeCreationAction.OnImageChange -> onRecipeInfoChange(newImageBytes = action.newImage?.toImmutableList())
            is ManualRecipeCreationAction.OnPrepTimeChange -> onPrepTimeChange(
                prepTimeHour = action.newPrepTimeHour,
                prepTimeMinute = action.newPrepTimeMinute
            )

            is ManualRecipeCreationAction.OnCookTimeChange -> onCookTimeChange(
                cookTimeHour = action.newCookTimeHour,
                cookTimeMinute = action.newCookTimeMinute
            )

            is ManualRecipeCreationAction.OnServingsChange -> onRecipeInfoChange(newServings = action.newServings)
            is ManualRecipeCreationAction.OnDifficultyChange -> onRecipeInfoChange(newDifficulty = action.newDifficulty)
            is ManualRecipeCreationAction.OnSourceUrlChange -> onRecipeInfoChange(newSourceUrl = action.newSourceUrl)

            // Ingredients sections actions
            is ManualRecipeCreationAction.OnCreateIngredientSection -> createIngredientSection(
                newSectionName = action.newSectionName
            )

            is ManualRecipeCreationAction.OnRenameIngredientSectionName -> renameIngredientSection(
                sectionId = action.sectionId,
                newName = action.newSectionName
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

            // Dialog actions
            ManualRecipeCreationAction.OnDismissDialog -> onDismissDialog()
            is ManualRecipeCreationAction.OnShowCreateOrEditIngredientSectionNameDialog -> onShowCreateOrEditIngredientSectionNameDialog(
                sectionId = action.sectionId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditMainIngredientDialog -> onShowCreateOrEditMainIngredientDialog(
                ingredientId = action.ingredientId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditSectionIngredientDialog -> onShowCreateOrEditSectionIngredientDialog(
                sectionId = action.sectionId,
                ingredientId = action.ingredientId
            )

            is ManualRecipeCreationAction.OnShowCreateOrEditInstructionDialog -> onShowCreateOrEditInstructionDialog(
                instructionId = action.instructionId
            )

            is ManualRecipeCreationAction.OnCreateOrEditInstruction -> createOrEditInstruction(
                instructionId = action.instructionId,
                title = action.title,
                instruction = action.instruction
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

    private fun createRecipe() {
        viewModelScope.launch {
            val mappedRecipe = state.value.recipe.toDomain()
            val image = state.value.recipe.info.image

            _isCreatingRecipe.emit(true)
            createManualRecipeWithImageUseCase(recipe = mappedRecipe, image = image)
                .onSuccess {
                    _event.emit(ManualRecipeCreationEvent.RecipeSuccessfullyCreated)
                }
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
                .also {
                    _isCreatingRecipe.emit(false)
                }
        }
    }

    private fun onPrepTimeChange(
        prepTimeHour: Int,
        prepTimeMinute: Int
    ) {
        _recipeInfo.update { currentState ->
            currentState.copy(prepTime = LocalTime(prepTimeHour, prepTimeMinute))
        }
    }

    private fun onCookTimeChange(
        cookTimeHour: Int,
        cookTimeMinute: Int
    ) {
        _recipeInfo.update { currentState ->
            currentState.copy(cookTime = LocalTime(cookTimeHour, cookTimeMinute))
        }
    }

    // Recipe info methods

    private fun onRecipeInfoChange(
        newTitle: String = _recipeInfo.value.title,
        newImageBytes: ImmutableList<Byte>? = _recipeInfo.value.image,
        newServings: String = _recipeInfo.value.servings,
        newDifficulty: RecipeDifficulty? = _recipeInfo.value.difficulty,
        newSourceUrl: String = _recipeInfo.value.sourceUrl
    ) {
        _recipeInfo.update { currentState ->
            currentState.copy(
                title = newTitle,
                image = newImageBytes,
                servings = newServings,
                difficulty = newDifficulty,
                sourceUrl = newSourceUrl
            )
        }
    }

    // Ingredients sections methods

    private fun createIngredientSection(newSectionName: String) {
        val oderIndex = _recipeIngredientSections.value.size
        val newSection = RecipeIngredientSectionUiData(
            name = newSectionName,
            ingredients = persistentListOf(),
            orderIndex = oderIndex
        )

        _recipeIngredientSections.update { currentSections ->
            (currentSections + newSection).toImmutableList()
        }
    }

    private fun renameIngredientSection(sectionId: String, newName: String) {
        _recipeIngredientSections.update { currentSections ->
            currentSections.map { section ->
                if (section.id == sectionId) {
                    section.copy(name = newName)
                } else {
                    section
                }
            }.toImmutableList()
        }
    }

    private fun deleteIngredientSection(sectionId: String) {
        _recipeIngredientSections.update { currentSections ->
            currentSections.filterNot { it.id == sectionId }.toImmutableList()
        }
    }

    // Ingredients methods

    private fun createOrEditMainIngredient(
        ingredientId: String? = null,
        name: String,
        quantity: String,
        unit: String,
        note: String
    ) {
        _mainIngredients.update { currentIngredients ->
            if (ingredientId == null) {
                // Create new ingredient
                val newIngredient = RecipeIngredientUiData(
                    id = Uuid.random().toString(),
                    name = name,
                    quantity = quantity,
                    unit = unit,
                    notes = note,
                    orderIndex = currentIngredients.size
                )

                (currentIngredients + newIngredient).toImmutableList()
            } else {
                // Edit existing ingredient
                currentIngredients.map { ingredient ->
                    if (ingredient.id != ingredientId) return@map ingredient

                    ingredient.copy(
                        name = name,
                        quantity = quantity,
                        unit = unit,
                        notes = note
                    )
                }.toImmutableList()
            }
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
        _recipeIngredientSections.update { currentSections ->
            currentSections.map { section ->
                if (section.id != sectionId) return@map section

                // If ingredientId is null, we are creating a new ingredient, otherwise,
                // we are editing an existing one
                val updatedIngredients = if (ingredientId != null) {
                    section.ingredients.map { ingredient ->
                        if (ingredient.id != ingredientId) return@map ingredient

                        ingredient.copy(
                            name = name,
                            quantity = quantity,
                            unit = unit,
                            notes = note
                        )
                    }
                } else {
                    section.ingredients + RecipeIngredientUiData(
                        id = Uuid.random().toString(),
                        name = name,
                        quantity = quantity,
                        unit = unit,
                        notes = note,
                        orderIndex = section.ingredients.size
                    )
                }

                section.copy(ingredients = updatedIngredients.toImmutableList())
            }.toImmutableList()
        }
    }

    // Instructions methods

    private fun createOrEditInstruction(
        instructionId: String? = null,
        title: String,
        instruction: String
    ) {
        _recipeInstructions.update { currentInstructions ->
            if (instructionId == null) {
                // Create new instruction
                val newInstruction = RecipeInstructionUiData(
                    id = Uuid.random().toString(),
                    title = title,
                    instruction = instruction,
                    orderIndex = currentInstructions.size
                )

                (currentInstructions + newInstruction).toImmutableList()
            } else {
                // Edit existing instruction
                currentInstructions.map { currentInstruction ->
                    if (currentInstruction.id != instructionId) return@map currentInstruction

                    currentInstruction.copy(
                        title = title,
                        instruction = instruction
                    )
                }.toImmutableList()
            }
        }
    }

    private fun deleteInstruction(instructionId: String) {
        _recipeInstructions.update { currentInstructions ->
            currentInstructions.filterNot { it.id == instructionId }.toImmutableList()
        }
    }

    // Dialog methods

    private fun onDismissDialog() {
        _dialogState.update { null }
    }

    private fun onShowCreateOrEditIngredientSectionNameDialog(sectionId: String? = null) {
        val section = sectionId?.let { id ->
            _recipeIngredientSections.value.firstOrNull { it.id == id } ?: return
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditIngredientSectionName(
                sectionId = sectionId,
                sectionName = section?.name
            )
        }
    }

    private fun onShowCreateOrEditMainIngredientDialog(ingredientId: String?) {
        val ingredient = ingredientId?.let { id ->
            _mainIngredients.value.firstOrNull { it.id == id } ?: return
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditMainIngredient(
                ingredient = ingredient
            )
        }
    }

    private fun onShowCreateOrEditSectionIngredientDialog(
        sectionId: String,
        ingredientId: String?
    ) {
        val currentSection = _recipeIngredientSections.value
            .firstOrNull { it.id == sectionId } ?: return
        val ingredient = ingredientId?.let { id ->
            currentSection.ingredients.firstOrNull { it.id == id }
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditIngredient(
                sectionId = sectionId,
                sectionName = currentSection.name,
                ingredient = ingredient
            )
        }
    }

    private fun onShowCreateOrEditInstructionDialog(instructionId: String?) {
        val instruction = instructionId?.let { id ->
            _recipeInstructions.value.firstOrNull { it.id == id }
        }

        _dialogState.update {
            ManualRecipeCreationDialogState.CreateOrEditInstruction(instruction = instruction)
        }
    }
}
