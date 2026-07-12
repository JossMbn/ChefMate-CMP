package com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.core.designsystem.component.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.PrimaryButton
import com.jmabilon.chefmate.core.designsystem.newcomponent.textfield.InputTextField
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetWithTitleContainer
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameCookbookBottomSheet(
    modifier: Modifier = Modifier,
    cookbookId: String,
    viewModel: RenameCookbookViewModel = koinViewModel(parameters = { parametersOf(cookbookId) }),
    onDismissRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val dismissRequester: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            RenameCookbookEvent.CookbookSuccessfullyRenamed -> dismissRequester()
        }
    }

    BottomSheetWithTitleContainer(
        modifier = modifier,
        label = "Rename Cookbook",
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        RenameCookbookBottomSheetContent(
            state = state.value,
            onAction = viewModel::onAction,
            onDismissRequest = dismissRequester
        )
    }
}

@Composable
private fun RenameCookbookBottomSheetContent(
    state: RenameCookbookState,
    onAction: (RenameCookbookAction) -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
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
                onValueChange = { onAction(RenameCookbookAction.OnCookbookNameChange(it)) },
                hint = "e.g., Deserts, Quick Meals, etc.",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Save name",
            enabled = state.cookbookName.isNotBlank(),
            onClick = {
                onAction(RenameCookbookAction.OnRenameCookbookClick)
                onDismissRequest()
            }
        )
    }
}
