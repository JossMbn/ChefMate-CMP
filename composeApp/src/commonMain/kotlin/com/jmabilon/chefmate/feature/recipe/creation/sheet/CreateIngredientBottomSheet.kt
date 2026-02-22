package com.jmabilon.chefmate.feature.recipe.creation.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.designsystem.component.textfield.DefaultFieldDecoration
import com.jmabilon.chefmate.designsystem.sheet.BottomSheetContainer
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeIngredientUiData
import com.jmabilon.chefmate.feature.recipe.creation.sheet.component.CreateRecipeSheetFooterButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateIngredientBottomSheet(
    modifier: Modifier = Modifier,
    sectionName: String? = null,
    ingredient: RecipeIngredientUiData?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String, String, String, String) -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        CreateIngredientBottomSheetContent(
            sectionName = sectionName,
            ingredient = ingredient,
            onDismissRequest = dismissRequester,
            onConfirmClick = onConfirmClick
        )
    }
}

@Composable
private fun CreateIngredientBottomSheetContent(
    modifier: Modifier = Modifier,
    sectionName: String?,
    ingredient: RecipeIngredientUiData?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String, String, String, String) -> Unit
) {
    val scrollState = rememberScrollState()

    val isEditing by remember(
        ingredient?.name,
        ingredient?.quantity
    ) { mutableStateOf(ingredient?.name != null || ingredient?.quantity != null) }
    val titleText by remember {
        derivedStateOf {
            if (isEditing) "Edit Ingredient" else "Add Ingredient"
        }
    }

    var ingredientName by remember(ingredient?.name) { mutableStateOf(ingredient?.name ?: "") }
    var ingredientQuantity by remember(ingredient?.quantity) {
        mutableStateOf(
            ingredient?.quantity ?: ""
        )
    }
    var ingredientUnit by remember(ingredient?.unit) { mutableStateOf(ingredient?.unit ?: "") }
    var ingredientNotes by remember(ingredient?.notes) { mutableStateOf(ingredient?.notes ?: "") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!sectionName.isNullOrEmpty()) {
                Text(
                    text = buildAnnotatedString {
                        append("for section :")
                        append(" ")

                        withStyle(
                            style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(sectionName)
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = ingredientName,
                onValueChange = { ingredientName = it },
                label = "Name",
                hint = "e.g., For the sauce",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CMTextField(
                    modifier = Modifier.weight(1f),
                    value = ingredientQuantity,
                    onValueChange = { newValue ->
                        val newIngredientQuantity = newValue
                            .filter { it.isDigit() }
                            .take(4) // Limit to 4 digits to prevent overflow
                        ingredientQuantity = newIngredientQuantity
                    },
                    label = "Quantity",
                    hint = "3",
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                CMTextField(
                    modifier = Modifier.weight(1f),
                    value = ingredientUnit,
                    onValueChange = { ingredientUnit = it },
                    label = "Unit",
                    hint = "g, ml, kg",
                    singleLine = true
                )
            }

            CMTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ingredientNotes,
                onValueChange = { ingredientNotes = it },
                label = "Preparation Notes (optional)",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            ) { innerTextField ->
                DefaultFieldDecoration(
                    modifier = Modifier
                        .height(130.dp),
                    innerField = innerTextField,
                    value = ingredientNotes,
                    hint = "e.g. chopped, sliced, etc."
                )
            }
        }

        CreateRecipeSheetFooterButtons(
            modifier = Modifier.fillMaxWidth(),
            primaryButtonLabel = "Confirm",
            secondaryButtonLabel = "Cancel",
            onPrimaryButtonClick = {
                onConfirmClick(
                    ingredientName,
                    ingredientQuantity,
                    ingredientUnit,
                    ingredientNotes
                )
                onDismissRequest()
            },
            onSecondaryButtonClick = { onDismissRequest() }
        )
    }
}

@Preview
@Composable
private fun CreateIngredientBottomSheetContentPreview() {
    ChefMateTheme {
        CreateIngredientBottomSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            sectionName = "",
            ingredient = null,
            onDismissRequest = { /* no-op */ },
            onConfirmClick = { _, _, _, _ -> /* no-op */ }
        )
    }
}
