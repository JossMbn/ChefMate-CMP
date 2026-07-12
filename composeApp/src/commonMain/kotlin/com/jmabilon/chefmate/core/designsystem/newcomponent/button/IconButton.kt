package com.jmabilon.chefmate.core.designsystem.newcomponent.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.core.designsystem.provider.compositionlocal.RemoveDefaultPaddingProvider
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    onClick: () -> Unit
) {
    RemoveDefaultPaddingProvider {
        IconButton(
            modifier = modifier
                .size(42.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                ),
            colors = colors,
            shape = MaterialTheme.shapes.medium,
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painter,
                contentDescription = contentDescription
            )
        }
    }
}

@Preview
@Composable
private fun IconButtonPreview() {
    ChefMateTheme {
        IconButton(
            painter = painterResource(Res.drawable.ic_add_rounded_fill),
            contentDescription = "Add a photo",
            onClick = { /* no-op */ }
        )
    }
}
