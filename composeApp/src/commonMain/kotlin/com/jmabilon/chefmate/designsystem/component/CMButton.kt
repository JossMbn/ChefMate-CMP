package com.jmabilon.chefmate.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@Composable
fun CMButton(
    modifier: Modifier = Modifier,
    label: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    border: BorderStroke? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = colors,
        border = border,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.invoke()

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )

            trailingContent?.invoke()
        }
    }
}

@Preview
@Composable
private fun RAButtonPreview() {
    ChefMateTheme {
        CMButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Get Started",
            onClick = { /* no-op */ }
        )
    }
}
