package com.jmabilon.chefmate.core.designsystem.newcomponent.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun FabButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(58.dp),
        shape = MaterialTheme.shapes.large,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painter,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
private fun FabButtonPreview() {
    ChefMateTheme {
        FabButton(
            painter = painterResource(Res.drawable.ic_add_rounded_fill),
            contentDescription = "Add a photo",
            onClick = { /* no-op */ }
        )
    }
}
