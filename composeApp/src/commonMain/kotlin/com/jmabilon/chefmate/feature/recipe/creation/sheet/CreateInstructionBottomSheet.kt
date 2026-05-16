package com.jmabilon.chefmate.feature.recipe.creation.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.component.sheet.BottomSheetFooterButtons
import com.jmabilon.chefmate.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.designsystem.component.textfield.DefaultFieldDecoration
import com.jmabilon.chefmate.designsystem.extension.negativePadding
import com.jmabilon.chefmate.designsystem.sheet.BottomSheetContainer
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.RecipeCreationInstructionUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInstructionBottomSheet(
    modifier: Modifier = Modifier,
    instruction: RecipeCreationInstructionUiModel?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String, String) -> Unit,
    onDeleteInstructionClick: () -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        CreateInstructionBottomSheetContent(
            instruction = instruction,
            onDismissRequest = dismissRequester,
            onConfirmClick = onConfirmClick,
            onDeleteInstructionClick = onDeleteInstructionClick
        )
    }
}

@Composable
private fun CreateInstructionBottomSheetContent(
    modifier: Modifier = Modifier,
    instruction: RecipeCreationInstructionUiModel?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String, String) -> Unit,
    onDeleteInstructionClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val isEditing by remember(
        instruction?.title,
        instruction?.instruction
    ) { mutableStateOf(instruction?.title != null || instruction?.instruction != null) }
    val titleText by remember {
        derivedStateOf {
            if (isEditing) "Edit Instruction" else "Add Instruction"
        }
    }

    var title by remember(instruction?.title) { mutableStateOf(instruction?.title ?: "") }
    var instruction by remember(instruction?.instruction) {
        mutableStateOf(
            instruction?.instruction ?: ""
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            text = titleText,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = "Title (optional)",
                hint = "e.g., Prepation, Cooking",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = instruction,
                onValueChange = { instruction = it },
                label = "Instruction",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            ) { innerTextField ->
                DefaultFieldDecoration(
                    modifier = Modifier
                        .height(200.dp),
                    innerField = innerTextField,
                    value = instruction,
                    hint = "e.g. Preheat the oven to 180°C. In a bowl, mix flour and sugar. Add eggs and stir until smooth.",
                )
            }
        }

        if (isEditing) {
            TextButton(
                modifier = Modifier.negativePadding(vertical = 20.dp),
                onClick = {
                    onDeleteInstructionClick()
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Delete Ingredient",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        BottomSheetFooterButtons(
            modifier = Modifier.fillMaxWidth(),
            primaryButtonLabel = "Confirm",
            secondaryButtonLabel = "Cancel",
            onPrimaryButtonClick = {
                onConfirmClick(
                    title,
                    instruction
                )
                onDismissRequest()
            },
            onSecondaryButtonClick = { onDismissRequest() }
        )
    }
}

@Preview
@Composable
private fun CreateInstructionBottomSheetContentPreview() {
    ChefMateTheme {
        CreateInstructionBottomSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            instruction = null,
            onDismissRequest = { /* no-op */ },
            onConfirmClick = { _, _ -> /* no-op */ },
            onDeleteInstructionClick = { /* no-op */ }
        )
    }
}
