package com.jmabilon.chefmate.feature.recipe.creation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import chefmate.composeapp.generated.resources.ic_sticky_note_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe.RecipeCreationIngredientUiModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun IngredientItem(
    modifier: Modifier = Modifier,
    ingredient: RecipeCreationIngredientUiModel,
    onEditClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .customClickable { onEditClick() }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = ingredient.displayText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!ingredient.note.isNullOrEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_sticky_note_rounded_outlined),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = ingredient.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun IngredientItemPreview() {
    ChefMateTheme {
        IngredientItem(
            ingredient = RecipeCreationIngredientUiModel(
                id = "0",
                name = "Chicken Thighs",
                quantity = "2",
                unit = "lbs",
                note = "Bone-in, skin-on preffered",
                orderIndex = 0
            ),
            onEditClick = { /* no-op */ }
        )
    }
}
