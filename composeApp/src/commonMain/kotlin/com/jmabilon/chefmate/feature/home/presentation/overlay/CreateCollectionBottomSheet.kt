package com.jmabilon.chefmate.feature.home.presentation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.component.sheet.BottomSheetFooterButtons
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.core.designsystem.sheet.BottomSheetContainer
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCollectionBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCreateCollectionClick: (String) -> Unit
) {
    BottomSheetContainer(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) { dismissRequester ->
        CreateCollectionBottomSheetContent(
            onDismissRequest = dismissRequester,
            onCreateCollectionClick = onCreateCollectionClick
        )
    }
}

@Composable
private fun CreateCollectionBottomSheetContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCreateCollectionClick: (String) -> Unit,
) {
    var collectionName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
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
            value = collectionName,
            onValueChange = { collectionName = it },
            label = "Collection name",
            hint = "e.g., Deserts, Quick Meals, etc.",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        BottomSheetFooterButtons(
            modifier = Modifier.fillMaxWidth(),
            primaryButtonLabel = "Create",
            secondaryButtonLabel = "Cancel",
            isPrimaryButtonEnabled = collectionName.isNotBlank(),
            onPrimaryButtonClick = {
                onCreateCollectionClick(collectionName)
                onDismissRequest()
            },
            onSecondaryButtonClick = { onDismissRequest() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateCollectionBottomSheetContentPreview() {
    ChefMateTheme {
        CreateCollectionBottomSheetContent(
            onDismissRequest = { /* no-op */ },
            onCreateCollectionClick = { /* no-op */ }
        )
    }
}
