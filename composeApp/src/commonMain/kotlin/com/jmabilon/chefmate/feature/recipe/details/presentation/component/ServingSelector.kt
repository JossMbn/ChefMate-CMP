package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import chefmate.composeapp.generated.resources.ic_remove_rounded
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionUiModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServingsSelector(
    modifier: Modifier = Modifier,
    currentServings: Int,
    onServingsChanged: (RecipeServingActionUiModel) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(
            onClick = { onServingsChanged(RecipeServingActionUiModel.Decrement) },
            enabled = currentServings > 1,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(Res.drawable.ic_remove_rounded),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.widthIn(min = 24.dp),
            text = currentServings.toString(),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(
            onClick = { onServingsChanged(RecipeServingActionUiModel.Increment) },
            enabled = currentServings < 20,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(Res.drawable.ic_add_rounded_fill),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ServingsSelectorPreview() {
    ChefMateTheme {
        ServingsSelector(
            currentServings = 4,
            onServingsChanged = { /* no-op */ }
        )
    }
}
