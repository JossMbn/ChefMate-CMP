package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import chefmate.composeapp.generated.resources.ic_remove_rounded
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.newcomponent.common.FieldLabelContainer
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServeSelector(
    modifier: Modifier = Modifier,
    serves: String,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit
) {
    val shape = MaterialTheme.shapes.medium

    FieldLabelContainer(
        modifier = modifier,
        label = "Serves"
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .clip(shape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = shape
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(
                        rippleEnabled = false,
                        onClick = onDecreaseClick
                    ),
                painter = painterResource(Res.drawable.ic_remove_rounded),
                contentDescription = "Decrease serve count",
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.weight(1f),
                text = serves,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(
                        rippleEnabled = false,
                        onClick = onIncreaseClick
                    ),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                contentDescription = "Increase serve count",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ServeSelectorPreview() {
    ChefMateTheme {
        ServeSelector(
            serves = "2",
            onDecreaseClick = { /* no-op */ },
            onIncreaseClick = { /* no-op */ }
        )
    }
}
