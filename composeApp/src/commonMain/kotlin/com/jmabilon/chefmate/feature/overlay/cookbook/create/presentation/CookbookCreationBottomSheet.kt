package com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.core.designsystem.component.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.PrimaryButton
import com.jmabilon.chefmate.core.designsystem.newcomponent.textfield.InputTextField
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetWithTitleContainer
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookCreationBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: CookbookCreationViewModel = koinViewModel(),
    onDismissRequest: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            is CookbookCreationEvent.OnCookbookCreated -> onDismissRequest()
        }
    }

    BottomSheetWithTitleContainer(
        modifier = modifier,
        label = "New cookbook",
        onDismissRequest = onDismissRequest
    ) {
        CookbookCreationBottomSheetContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun CookbookCreationBottomSheetContent(
    modifier: Modifier = Modifier,
    state: CookbookCreationState,
    onAction: (CookbookCreationAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .padding(bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FieldLabelContainer(
            label = "Cookbook name"
        ) {
            InputTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.cookbookName,
                onValueChange = { onAction(CookbookCreationAction.OnCookbookNameChange(name = it)) },
                hint = "e.g., Deserts, Quick Meals, etc.",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Create",
            enabled = state.cookbookName.isNotBlank(),
            onClick = {
                onAction(CookbookCreationAction.OnCreateCookbookClick)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CookbookCreationBottomSheetContentPreview() {
    ChefMateTheme {
        CookbookCreationBottomSheetContent(
            state = CookbookCreationState(),
            onAction = { /* no-op */ }
        )
    }
}
