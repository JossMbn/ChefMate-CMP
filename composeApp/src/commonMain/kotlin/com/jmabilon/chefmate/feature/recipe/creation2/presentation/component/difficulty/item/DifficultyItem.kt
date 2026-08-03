package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.difficulty.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@Composable
fun DifficultyItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = MaterialTheme.shapes.medium

    val borderColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        label = "Border Color Animation"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = "Background Color Animation"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "Text Color Animation"
    )

    Box(
        modifier = modifier
            .heightIn(min = 60.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .background(backgroundColor)
            .customClickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun DifficultyItemPreview() {
    ChefMateTheme {
        DifficultyItem(
            text = "Easy",
            selected = true,
            onClick = { /* no-op */ }
        )
    }
}
