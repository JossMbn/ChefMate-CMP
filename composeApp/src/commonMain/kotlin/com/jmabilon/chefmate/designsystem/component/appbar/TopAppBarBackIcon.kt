package com.jmabilon.chefmate.designsystem.component.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopAppBarBackIcon(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(Res.drawable.ic_arrow_back_rounded_outlined),
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun TopAppBarBackIconPreview() {
    ChefMateTheme {
        TopAppBarBackIcon(
            onClick = { /* no-op */ }
        )
    }
}
