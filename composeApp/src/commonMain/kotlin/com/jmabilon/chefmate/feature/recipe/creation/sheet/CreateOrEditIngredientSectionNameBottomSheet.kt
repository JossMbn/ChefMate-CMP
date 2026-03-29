package com.jmabilon.chefmate.feature.recipe.creation.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.jmabilon.chefmate.designsystem.sheet.BottomSheetContainer
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditIngredientSectionNameBottomSheet(
    modifier: Modifier = Modifier,
    sectionId: String?,
    sectionName: String?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (sectionId: String?, newSectionName: String) -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        CreateOrEditIngredientSectionNameBottomSheetContent(
            sectionId = sectionId,
            sectionName = sectionName,
            onDismissRequest = dismissRequester,
            onConfirmClick = onConfirmClick
        )
    }
}

@Composable
private fun CreateOrEditIngredientSectionNameBottomSheetContent(
    modifier: Modifier = Modifier,
    sectionId: String?,
    sectionName: String?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (sectionId: String?, newSectionName: String) -> Unit
) {
    var editableSectionName by remember(sectionName) { mutableStateOf(sectionName ?: "") }
    val isEditing by remember(sectionName) { mutableStateOf(sectionName != null) }
    val titleText by remember {
        derivedStateOf {
            if (isEditing) "Edit Section" else "Add Section"
        }
    }
    val descriptionText by remember {
        derivedStateOf {
            if (isEditing) {
                "Update the name of your ingredient section."
            } else {
                "Organize your ingredients into sections."
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = descriptionText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        CMTextField(
            modifier = Modifier.fillMaxWidth(),
            value = editableSectionName,
            onValueChange = { editableSectionName = it },
            label = "Section name",
            hint = "e.g., For the sauce",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        BottomSheetFooterButtons(
            modifier = Modifier.fillMaxWidth(),
            primaryButtonLabel = "Confirm",
            secondaryButtonLabel = "Cancel",
            onPrimaryButtonClick = {
                onConfirmClick(sectionId, editableSectionName)
                onDismissRequest()
            },
            onSecondaryButtonClick = { onDismissRequest() }
        )
    }
}

@Preview
@Composable
private fun CreateOrEditIngredientSectionNameBottomSheetContentPreview() {
    ChefMateTheme {
        CreateOrEditIngredientSectionNameBottomSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            sectionId = null,
            sectionName = null,
            onDismissRequest = { /* no-op */ },
            onConfirmClick = { _, _ -> /* no-op */ }
        )
    }
}
