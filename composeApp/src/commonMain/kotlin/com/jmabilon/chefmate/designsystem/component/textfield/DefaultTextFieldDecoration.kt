package com.jmabilon.chefmate.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme

@Composable
fun DefaultFieldDecoration(
    modifier: Modifier = Modifier,
    innerField: @Composable () -> Unit,
    value: String,
    hint: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        leadingContent?.invoke()

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty() && !hint.isNullOrEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            innerField()
        }

        trailingContent?.invoke()
    }
}

@Preview
@Composable
private fun DefaultFieldDecorationPreview() {
    ChefMateTheme {
        val value by remember { mutableStateOf("") }

        DefaultFieldDecoration(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            innerField = {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            value = value,
            hint = "chef@example.com"
        )
    }
}
