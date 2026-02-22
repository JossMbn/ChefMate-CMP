package com.jmabilon.chefmate.feature.recipe.creation.sheet.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.component.button.CMButton

@Composable
fun CreateRecipeSheetFooterButtons(
    modifier: Modifier = Modifier,
    primaryButtonLabel: String,
    secondaryButtonLabel: String,
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
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            onClick = onSecondaryButtonClick
        )

        CMButton(
            modifier = Modifier.weight(1f),
            label = primaryButtonLabel,
            onClick = onPrimaryButtonClick
        )
    }
}
