package com.jmabilon.chefmate.designsystem.component.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.component.button.CMButton
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@Composable
fun BottomSheetFooterButtons(
    modifier: Modifier = Modifier,
    primaryButtonLabel: String,
    secondaryButtonLabel: String,
    isPrimaryButtonEnabled: Boolean = true,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CMButton(
            modifier = Modifier.weight(1f),
            label = secondaryButtonLabel,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            onClick = onSecondaryButtonClick
        )

        CMButton(
            modifier = Modifier.weight(1f),
            label = primaryButtonLabel,
            enabled = isPrimaryButtonEnabled,
            onClick = onPrimaryButtonClick
        )
    }
}

@Preview
@Composable
private fun BottomSheetFooterButtonsPreview() {
    ChefMateTheme {
        BottomSheetFooterButtons(
            primaryButtonLabel = "Save",
            secondaryButtonLabel = "Cancel",
            onPrimaryButtonClick = { /* no-op */ },
            onSecondaryButtonClick = { /* no-op */ }
        )
    }
}
