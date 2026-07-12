package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ingredient

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import chefmate.composeapp.generated.resources.ic_remove_rounded
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServingStepper(
    modifier: Modifier = Modifier,
    serving: String,
    onRemoveClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.extraLarge
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 6.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.customClickable(rippleEnabled = false, onClick = onRemoveClick),
            painter = painterResource(Res.drawable.ic_remove_rounded),
            contentDescription = "Decrease servings",
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300)
                ),
            text = serving,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            modifier = Modifier.customClickable(rippleEnabled = false, onClick = onAddClick),
            painter = painterResource(Res.drawable.ic_add_rounded_fill),
            contentDescription = "Increase servings",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun ServingStepperPreview() {
    ChefMateTheme {
        ServingStepper(
            serving = "2",
            onRemoveClick = { /* no-op */ },
            onAddClick = { /* no-op */ }
        )
    }
}
