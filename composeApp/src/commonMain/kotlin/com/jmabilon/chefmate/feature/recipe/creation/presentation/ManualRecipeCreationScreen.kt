package com.jmabilon.chefmate.feature.recipe.creation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_group_rounded_outlined
import chefmate.composeapp.generated.resources.ic_link_rounded_outlined
import chefmate.composeapp.generated.resources.ic_schedule_rounded_outlined
import chefmate.composeapp.generated.resources.ic_timer_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.component.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.component.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.component.appbar.TopAppBarBackIcon
import com.jmabilon.chefmate.core.designsystem.component.button.AddTextButton
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextFieldIcon
import com.jmabilon.chefmate.core.designsystem.provider.rememberImagePicker
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.feature.recipe.creation.presentation.component.IngredientMainSectionContainer
import com.jmabilon.chefmate.feature.recipe.creation.presentation.component.IngredientsSectionContainer
import com.jmabilon.chefmate.feature.recipe.creation.presentation.component.InstructionItem
import com.jmabilon.chefmate.feature.recipe.creation.presentation.component.RecipeImageContainer
import com.jmabilon.chefmate.feature.recipe.creation.presentation.component.TimeInputField
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.ManualRecipeCreationContext
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.ManualRecipeCreationDialogState
import com.jmabilon.chefmate.feature.recipe.creation.presentation.overlay.CreateIngredientBottomSheet
import com.jmabilon.chefmate.feature.recipe.creation.presentation.overlay.CreateInstructionBottomSheet
import com.jmabilon.chefmate.feature.recipe.creation.presentation.overlay.CreateOrEditIngredientSectionNameBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManualRecipeCreationRoot(
    viewModel: ManualRecipeCreationViewModel = koinViewModel(),
    navigator: ManualRecipeCreationNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            ManualRecipeCreationEvent.RecipeSuccessfullyCreatedOrUpdated -> navigator.navigateBack()
        }
    }

    ManualRecipeCreationScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun ManualRecipeCreationScreen(
    state: ManualRecipeCreationState,
    onAction: (ManualRecipeCreationAction) -> Unit,
    navigator: ManualRecipeCreationNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = if (state.context == ManualRecipeCreationContext.Creation) "New Recipe" else "Edit Recipe",
                navigationIcon = {
                    TopAppBarBackIcon(onClick = navigator::navigateBack)
                },
                actions = {
                    TextButton(
                        onClick = { onAction(ManualRecipeCreationAction.OnCreateRecipeClick) }
                    ) {
                        if (state.isCreatingRecipe) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (state.context == ManualRecipeCreationContext.Creation) "Create" else "Save",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        ManualRecipeCreationScreenContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction
        )
    }

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is ManualRecipeCreationDialogState.CreateOrEditIngredientSectionName -> {
                CreateOrEditIngredientSectionNameBottomSheet(
                    sectionId = dialogState.sectionId,
                    sectionName = dialogState.sectionName,
                    onDismissRequest = { onAction(ManualRecipeCreationAction.OnDismissDialog) },
                    onConfirmClick = { sectionId, newSectionName ->
                        if (sectionId == null) {
                            onAction(
                                ManualRecipeCreationAction.OnCreateIngredientSection(
                                    newSectionName = newSectionName
                                )
                            )
                        } else {
                            onAction(
                                ManualRecipeCreationAction.OnRenameIngredientSectionName(
                                    sectionId = sectionId,
                                    newSectionName = newSectionName
                                )
                            )
                        }
                    },
                    onDeleteSectionClick = { sectionId ->
                        onAction(ManualRecipeCreationAction.OnRemoveIngredientSectionClick(sectionId))
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditIngredient -> {
                CreateIngredientBottomSheet(
                    sectionName = dialogState.sectionName,
                    ingredient = dialogState.ingredient,
                    onDismissRequest = { onAction(ManualRecipeCreationAction.OnDismissDialog) },
                    onConfirmClick = { ingredientName, quantity, unit, note ->
                        if (dialogState.ingredient != null) {
                            onAction(
                                ManualRecipeCreationAction.OnEditSectionIngredient(
                                    sectionId = dialogState.sectionId,
                                    ingredientId = dialogState.ingredient.id,
                                    name = ingredientName,
                                    quantity = quantity,
                                    unit = unit,
                                    note = note
                                )
                            )
                        } else {
                            onAction(
                                ManualRecipeCreationAction.OnAddSectionIngredient(
                                    sectionId = dialogState.sectionId,
                                    name = ingredientName,
                                    quantity = quantity,
                                    unit = unit,
                                    note = note
                                )
                            )
                        }
                    },
                    onDeleteIngredientClick = {
                        if (dialogState.ingredient == null) return@CreateIngredientBottomSheet

                        onAction(
                            ManualRecipeCreationAction.OnRemoveIngredient(
                                ingredientId = dialogState.ingredient.id,
                                sectionId = dialogState.sectionId
                            )
                        )
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditMainIngredient -> {
                CreateIngredientBottomSheet(
                    ingredient = dialogState.ingredient,
                    onDismissRequest = { onAction(ManualRecipeCreationAction.OnDismissDialog) },
                    onConfirmClick = { ingredientName, quantity, unit, note ->
                        onAction(
                            ManualRecipeCreationAction.OnCreateOrEditMainIngredient(
                                ingredientId = dialogState.ingredient?.id,
                                name = ingredientName,
                                quantity = quantity,
                                unit = unit,
                                note = note
                            )
                        )
                    },
                    onDeleteIngredientClick = {
                        if (dialogState.ingredient == null) return@CreateIngredientBottomSheet

                        onAction(
                            ManualRecipeCreationAction.OnRemoveIngredient(
                                ingredientId = dialogState.ingredient.id
                            )
                        )
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditInstruction -> {
                CreateInstructionBottomSheet(
                    instruction = dialogState.instruction,
                    onDismissRequest = { onAction(ManualRecipeCreationAction.OnDismissDialog) },
                    onConfirmClick = { title, instruction ->
                        onAction(
                            ManualRecipeCreationAction.OnCreateOrEditInstruction(
                                instructionId = dialogState.instruction?.id,
                                title = title,
                                instruction = instruction
                            )
                        )
                    },
                    onDeleteInstructionClick = {
                        if (dialogState.instruction == null) return@CreateInstructionBottomSheet

                        onAction(
                            ManualRecipeCreationAction.OnRemoveInstruction(
                                instructionId = dialogState.instruction.id
                            )
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManualRecipeCreationScreenContent(
    modifier: Modifier = Modifier,
    state: ManualRecipeCreationState,
    onAction: (ManualRecipeCreationAction) -> Unit
) {
    val imagePicker = rememberImagePicker(
        onImagePicked = { newImage ->
            onAction(ManualRecipeCreationAction.OnImageChange(newImage))
        }
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            RecipeImageContainer(
                imageSource = state.recipe.imageSource,
                onEditClick = { imagePicker.pickImage() },
                onDeleteClick = { onAction(ManualRecipeCreationAction.OnImageChange(null)) }
            )
        }

        item {
            CMTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                value = state.recipe.title,
                onValueChange = { onAction(ManualRecipeCreationAction.OnTitleChange(it)) },
                label = "Title",
                hint = "e.g., Grandma's Apple Pie",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TimeInputField(
                    modifier = Modifier.weight(1f),
                    hour = state.recipe.prepTime.hour,
                    minute = state.recipe.prepTime.minute,
                    onValueChange = { hour, minute ->
                        onAction(ManualRecipeCreationAction.OnPrepTimeChange(hour, minute))
                    },
                    label = "Prep Time",
                    hint = "e.g., 10 min",
                    singleLine = true,
                    leadingContent = {
                        CMTextFieldIcon(icon = painterResource(Res.drawable.ic_schedule_rounded_outlined))
                    }
                )

                TimeInputField(
                    modifier = Modifier.weight(1f),
                    hour = state.recipe.cookTime.hour,
                    minute = state.recipe.cookTime.minute,
                    onValueChange = { hour, minute ->
                        onAction(ManualRecipeCreationAction.OnCookTimeChange(hour, minute))
                    },
                    label = "Cook Time",
                    hint = "e.g., 1 h 30 min",
                    singleLine = true,
                    leadingContent = {
                        CMTextFieldIcon(icon = painterResource(Res.drawable.ic_timer_rounded_outlined))
                    }
                )
            }
        }

        item {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.recipe.servings,
                onValueChange = { newValue ->
                    val newServingsValue = newValue
                        .filter { it.isDigit() }
                        .take(4) // Limit to 4 digits to prevent overflow
                    onAction(ManualRecipeCreationAction.OnServingsChange(newServingsValue))
                },
                label = "Servings",
                hint = "4 people",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                leadingContent = {
                    CMTextFieldIcon(icon = painterResource(Res.drawable.ic_group_rounded_outlined))
                }
            )
        }

        item {
            FieldLabelContainer(label = "Difficulty") {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecipeDifficulty.entries.forEach { difficulty ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = difficulty.ordinal,
                                count = RecipeDifficulty.entries.size
                            ),
                            label = {
                                Text(
                                    text = difficulty.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            selected = difficulty.ordinal == state.recipe.difficulty,
                            onClick = {
                                onAction(
                                    ManualRecipeCreationAction.OnDifficultyChange(
                                        difficulty.ordinal
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        item {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.recipe.sourceUrl,
                onValueChange = { onAction(ManualRecipeCreationAction.OnSourceUrlChange(it)) },
                label = "Source URL",
                hint = "https://...",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                ),
                leadingContent = {
                    CMTextFieldIcon(icon = painterResource(Res.drawable.ic_link_rounded_outlined))
                }
            )
        }

        item {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }

        item {
            IngredientMainSectionContainer(
                mainIngredients = state.recipe.mainIngredients,
                onAddSectionClick = { onAction(ManualRecipeCreationAction.OnShowCreateOrEditIngredientSectionNameDialog()) },
                onEditIngredientClick = { ingredientId ->
                    onAction(
                        ManualRecipeCreationAction.OnShowCreateOrEditMainIngredientDialog(
                            ingredientId = ingredientId
                        )
                    )
                },
                onAddMainIngredientClick = {
                    onAction(ManualRecipeCreationAction.OnShowCreateOrEditMainIngredientDialog())
                }
            )
        }

        items(state.recipe.ingredientSections) { section ->
            IngredientsSectionContainer(
                modifier = Modifier.fillMaxWidth(),
                section = section,
                onEditSection = {
                    onAction(
                        ManualRecipeCreationAction.OnShowCreateOrEditIngredientSectionNameDialog(
                            sectionId = section.id
                        )
                    )
                },
                onAddIngredientClick = {
                    onAction(
                        ManualRecipeCreationAction.OnShowCreateOrEditSectionIngredientDialog(
                            sectionId = section.id
                        )
                    )
                },
                onEditIngredientClick = { ingredientId ->
                    onAction(
                        ManualRecipeCreationAction.OnShowCreateOrEditSectionIngredientDialog(
                            sectionId = section.id,
                            ingredientId = ingredientId
                        )
                    )
                }
            )
        }

        item {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                AddTextButton(
                    label = "Add Instruction",
                    onClick = { onAction(ManualRecipeCreationAction.OnShowCreateOrEditInstructionDialog()) }
                )
            }
        }

        items(state.recipe.instructions) { instruction ->
            InstructionItem(
                modifier = Modifier.fillMaxWidth(),
                title = instruction.title,
                instruction = instruction.instruction,
                index = instruction.orderIndex + 1,
                onEditInstructionClick = {
                    onAction(
                        ManualRecipeCreationAction.OnShowCreateOrEditInstructionDialog(
                            instructionId = instruction.id
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun ManualRecipeCreationScreenPreview() {
    ChefMateTheme {
        ManualRecipeCreationScreen(
            state = ManualRecipeCreationState(),
            onAction = { /* no-op */ },
            navigator = ManualRecipeCreationNavigatorImpl()
        )
    }
}
