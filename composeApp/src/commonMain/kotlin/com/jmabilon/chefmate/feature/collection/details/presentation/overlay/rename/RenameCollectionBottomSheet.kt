package com.jmabilon.chefmate.feature.collection.details.presentation.overlay.rename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.core.designsystem.component.sheet.BottomSheetFooterButtons
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetContainer
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameCollectionBottomSheet(
    modifier: Modifier = Modifier,
    collectionId: String,
    viewModel: RenameCollectionViewModel = koinViewModel(),
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

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.onAction(RenameCollectionAction.OnSheetStarted(collectionId = collectionId))
    }

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            RenameCollectionEvent.CollectionSuccessfullyRenamed -> dismissRequester()
        }
    }

    BottomSheetContainer(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        RenameCollectionBottomSheetContent(
            state = state.value,
            onAction = viewModel::onAction,
            onDismissRequest = dismissRequester
        )
    }
}

@Composable
private fun RenameCollectionBottomSheetContent(
    state: RenameCollectionState,
    onAction: (RenameCollectionAction) -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New collection",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        CMTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.collectionName,
            onValueChange = { onAction(RenameCollectionAction.OnCollectionNameChange(it)) },
            label = "Collection name",
            hint = "e.g., Deserts, Quick Meals, etc.",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        BottomSheetFooterButtons(
            modifier = Modifier.fillMaxWidth(),
            primaryButtonLabel = "Rename",
            secondaryButtonLabel = "Cancel",
            isPrimaryButtonEnabled = state.collectionName.isNotBlank(),
            onPrimaryButtonClick = {
                onAction(RenameCollectionAction.OnRenameCollectionClick)
            },
            onSecondaryButtonClick = { onDismissRequest() }
        )
    }
}
