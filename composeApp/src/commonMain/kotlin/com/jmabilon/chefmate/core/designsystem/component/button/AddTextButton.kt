package com.jmabilon.chefmate.core.designsystem.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddTextButton(
    modifier: Modifier = Modifier,
    label: String? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(Res.drawable.ic_add_rounded_outlined),
            contentDescription = null
        )

        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun AddTextButtonPreview() {
    ChefMateTheme {
        AddTextButton(
            label = null,
            onClick = { /* No-op for preview */ }
        )
    }
}
