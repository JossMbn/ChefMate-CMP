package com.jmabilon.chefmate.feature.recipe.creation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_edit_rounded
import com.jmabilon.chefmate.designsystem.component.button.AddTextButton
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeIngredientSectionUiData
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource

@Composable
fun IngredientsSectionContainer(
    modifier: Modifier = Modifier,
    section: RecipeIngredientSectionUiData,
    onEditSection: () -> Unit,
    onAddIngredientClick: () -> Unit,
    onEditIngredientClick: (ingredientId: String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = section.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = onEditSection) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit_rounded),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        IngredientListContainer(
            ingredients = section.ingredients,
            onEditIngredientClick = onEditIngredientClick
        )

        AddTextButton(
            label = "Add Ingredient",
            onClick = onAddIngredientClick
        )
    }
}

@Preview
@Composable
private fun IngredientsSectionContainerPreview() {
    ChefMateTheme {
        IngredientsSectionContainer(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            section = RecipeIngredientSectionUiData(
                id = "1",
                name = "Section 1",
                ingredients = persistentListOf(),
                orderIndex = 0
            ),
            onEditSection = { /* no-op */ },
            onAddIngredientClick = { /* no-op */ },
            onEditIngredientClick = { /* no-op */ }
        )
    }
}
