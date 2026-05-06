package com.jmabilon.chefmate.designsystem.component.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopAppBarActionIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
private fun TopAppBarActionIconPreview() {
    TopAppBarActionIcon(
        painter = painterResource(Res.drawable.ic_arrow_back_rounded_outlined),
        contentDescription = null,
        onClick = { /* no-op */ }
    )
}
