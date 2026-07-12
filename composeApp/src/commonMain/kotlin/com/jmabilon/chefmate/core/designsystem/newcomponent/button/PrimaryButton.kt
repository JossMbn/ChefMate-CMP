package com.jmabilon.chefmate.core.designsystem.newcomponent.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_favorite_rounded_fill
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    leadingIcon: Painter? = null,
    onClick: () -> Unit
) {
    val enabledColor = MaterialTheme.colorScheme.primary
    val border = remember(enabled) {
        val color = if (enabled) enabledColor else Color.Unspecified
        BorderStroke(1.dp, color)
    }

    Button(
        modifier = modifier
            .heightIn(min = 48.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        border = border,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = leadingIcon,
                    contentDescription = null
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    ChefMateTheme {
        PrimaryButton(
            label = "Primary action",
            leadingIcon = painterResource(Res.drawable.ic_favorite_rounded_fill),
            onClick = { /* no-op */ }
        )
    }
}
