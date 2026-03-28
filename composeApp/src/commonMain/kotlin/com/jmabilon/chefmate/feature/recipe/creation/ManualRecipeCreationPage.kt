package com.jmabilon.chefmate.feature.recipe.creation

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
import chefmate.composeapp.generated.resources.ic_group_rounded
import chefmate.composeapp.generated.resources.ic_link_rounded
import chefmate.composeapp.generated.resources.ic_schedule_rounded
import chefmate.composeapp.generated.resources.ic_timer_rounded
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.designsystem.component.CMTopAppBar
import com.jmabilon.chefmate.designsystem.component.FieldLabelContainer
import com.jmabilon.chefmate.designsystem.component.button.AddTextButton
import com.jmabilon.chefmate.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.designsystem.component.textfield.CMTextFieldIcon
import com.jmabilon.chefmate.designsystem.provider.rememberImagePicker
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.domain.recipe.model.RecipeDifficulty
import com.jmabilon.chefmate.feature.recipe.creation.component.IngredientMainSectionContainer
import com.jmabilon.chefmate.feature.recipe.creation.component.IngredientsSectionContainer
import com.jmabilon.chefmate.feature.recipe.creation.component.InstructionItem
import com.jmabilon.chefmate.feature.recipe.creation.component.RecipeImageContainer
import com.jmabilon.chefmate.feature.recipe.creation.component.TimeInputField
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnAddSectionIngredient
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnCreateIngredientSection
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnCreateOrEditMainIngredient
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnDismissDialog
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnEditSectionIngredient
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnRenameIngredientSectionName
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnShowCreateOrEditIngredientSectionNameDialog
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnShowCreateOrEditMainIngredientDialog
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationAction.OnShowCreateOrEditSectionIngredientDialog
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationDialogState
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationEvent
import com.jmabilon.chefmate.feature.recipe.creation.model.ManualRecipeCreationState
import com.jmabilon.chefmate.feature.recipe.creation.navigation.ManualRecipeCreationNavigator
import com.jmabilon.chefmate.feature.recipe.creation.navigation.ManualRecipeCreationNavigatorImpl
import com.jmabilon.chefmate.feature.recipe.creation.sheet.CreateIngredientBottomSheet
import com.jmabilon.chefmate.feature.recipe.creation.sheet.CreateInstructionBottomSheet
import com.jmabilon.chefmate.feature.recipe.creation.sheet.CreateOrEditIngredientSectionNameBottomSheet
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
            ManualRecipeCreationEvent.RecipeSuccessfullyCreated -> navigator.navigateBack()
        }
    }

    ManualRecipeCreationPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun ManualRecipeCreationPage(
    state: ManualRecipeCreationState,
    onAction: (ManualRecipeCreationAction) -> Unit,
    navigator: ManualRecipeCreationNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "New Recipe",
                onNavigationClick = { navigator.navigateBack() },
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
                                text = "Create",
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
        ManualRecipeCreationPageContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManualRecipeCreationPageContent(
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
                image = state.recipe.info.image,
                onEditClick = { imagePicker.pickImage() },
                onDeleteClick = { onAction(ManualRecipeCreationAction.OnImageChange(null)) }
            )
        }

        item {
            CMTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                value = state.recipe.info.title,
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
                    hour = state.recipe.info.prepTime?.hour,
                    minute = state.recipe.info.prepTime?.minute,
                    onValueChange = { hour, minute ->
                        onAction(ManualRecipeCreationAction.OnPrepTimeChange(hour, minute))
                    },
                    label = "Prep Time",
                    hint = "e.g., 10 min",
                    singleLine = true,
                    leadingContent = {
                        CMTextFieldIcon(icon = painterResource(Res.drawable.ic_schedule_rounded))
                    }
                )

                TimeInputField(
                    modifier = Modifier.weight(1f),
                    hour = state.recipe.info.cookTime?.hour,
                    minute = state.recipe.info.cookTime?.minute,
                    onValueChange = { hour, minute ->
                        onAction(ManualRecipeCreationAction.OnCookTimeChange(hour, minute))
                    },
                    label = "Cook Time",
                    hint = "e.g., 1 h 30 min",
                    singleLine = true,
                    leadingContent = {
                        CMTextFieldIcon(icon = painterResource(Res.drawable.ic_timer_rounded))
                    }
                )
            }
        }

        item {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.recipe.info.servings,
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
                    CMTextFieldIcon(icon = painterResource(Res.drawable.ic_group_rounded))
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
                            selected = difficulty == state.recipe.info.difficulty,
                            onClick = {
                                onAction(
                                    ManualRecipeCreationAction.OnDifficultyChange(
                                        difficulty
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
                value = state.recipe.info.sourceUrl,
                onValueChange = { onAction(ManualRecipeCreationAction.OnSourceUrlChange(it)) },
                label = "Source URL",
                hint = "https://...",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                ),
                leadingContent = {
                    CMTextFieldIcon(icon = painterResource(Res.drawable.ic_link_rounded))
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
                onAddSectionClick = { onAction(OnShowCreateOrEditIngredientSectionNameDialog()) },
                onEditIngredientClick = { ingredientId ->
                    onAction(
                        OnShowCreateOrEditMainIngredientDialog(
                            ingredientId = ingredientId
                        )
                    )
                },
                onAddMainIngredientClick = {
                    onAction(OnShowCreateOrEditMainIngredientDialog())
                }
            )
        }

        items(state.recipe.ingredientSections) { section ->
            IngredientsSectionContainer(
                modifier = Modifier.fillMaxWidth(),
                section = section,
                onEditSection = {
                    onAction(
                        OnShowCreateOrEditIngredientSectionNameDialog(
                            sectionId = section.id
                        )
                    )
                },
                onAddIngredientClick = {
                    onAction(
                        OnShowCreateOrEditSectionIngredientDialog(
                            sectionId = section.id
                        )
                    )
                },
                onEditIngredientClick = { ingredientId ->
                    onAction(
                        OnShowCreateOrEditSectionIngredientDialog(
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

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is ManualRecipeCreationDialogState.CreateOrEditIngredientSectionName -> {
                CreateOrEditIngredientSectionNameBottomSheet(
                    sectionId = dialogState.sectionId,
                    sectionName = dialogState.sectionName,
                    onDismissRequest = { onAction(OnDismissDialog) },
                    onConfirmClick = { sectionId, newSectionName ->
                        if (sectionId == null) {
                            onAction(
                                OnCreateIngredientSection(
                                    newSectionName = newSectionName
                                )
                            )
                        } else {
                            onAction(
                                OnRenameIngredientSectionName(
                                    sectionId = sectionId,
                                    newSectionName = newSectionName
                                )
                            )
                        }
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditIngredient -> {
                CreateIngredientBottomSheet(
                    sectionName = dialogState.sectionName,
                    ingredient = dialogState.ingredient,
                    onDismissRequest = { onAction(OnDismissDialog) },
                    onConfirmClick = { ingredientName, quantity, unit, note ->
                        if (dialogState.ingredient != null) {
                            onAction(
                                OnEditSectionIngredient(
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
                                OnAddSectionIngredient(
                                    sectionId = dialogState.sectionId,
                                    name = ingredientName,
                                    quantity = quantity,
                                    unit = unit,
                                    note = note
                                )
                            )
                        }
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditMainIngredient -> {
                CreateIngredientBottomSheet(
                    ingredient = dialogState.ingredient,
                    onDismissRequest = { onAction(OnDismissDialog) },
                    onConfirmClick = { ingredientName, quantity, unit, note ->
                        onAction(
                            OnCreateOrEditMainIngredient(
                                ingredientId = dialogState.ingredient?.id,
                                name = ingredientName,
                                quantity = quantity,
                                unit = unit,
                                note = note
                            )
                        )
                    }
                )
            }

            is ManualRecipeCreationDialogState.CreateOrEditInstruction -> {
                CreateInstructionBottomSheet(
                    instruction = dialogState.instruction,
                    onDismissRequest = { onAction(OnDismissDialog) },
                    onConfirmClick = { title, instruction ->
                        onAction(
                            ManualRecipeCreationAction.OnCreateOrEditInstruction(
                                instructionId = dialogState.instruction?.id,
                                title = title,
                                instruction = instruction
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ManualRecipeCreationPagePreview() {
    ChefMateTheme {
        ManualRecipeCreationPage(
            state = ManualRecipeCreationState(),
            onAction = { /* no-op */ },
            navigator = ManualRecipeCreationNavigatorImpl()
        )
    }
}
