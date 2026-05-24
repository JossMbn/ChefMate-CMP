package com.jmabilon.chefmate.core.designsystem.component.textfield

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun CMTextFieldIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    tint: Color? = null
) {
    Icon(
        modifier = modifier.size(20.dp),
        painter = icon,
        contentDescription = null,
        tint = tint ?: MaterialTheme.colorScheme.onSurfaceVariant
    )
}
