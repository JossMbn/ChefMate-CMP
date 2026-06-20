package com.jmabilon.chefmate.feature.recipe.creation.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.component.button.AddTextButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe.RecipeCreationIngredientUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun IngredientMainSectionContainer(
    modifier: Modifier = Modifier,
    mainIngredients: ImmutableList<RecipeCreationIngredientUiModel>,
    onAddSectionClick: () -> Unit,
    onEditIngredientClick: (ingredientId: String) -> Unit,
    onAddMainIngredientClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            AddTextButton(
                label = "Add Section",
                onClick = onAddSectionClick
            )
        }

        IngredientListContainer(
            ingredients = mainIngredients,
            onEditIngredientClick = onEditIngredientClick
        )

        AddTextButton(
            label = "Add main Ingredient",
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onAddMainIngredientClick
        )
    }
}

@Preview
@Composable
private fun IngredientMainSectionContainerPreview() {
    ChefMateTheme {
        IngredientMainSectionContainer(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            mainIngredients = listOf(
                RecipeCreationIngredientUiModel(
                    id = "1",
                    name = "Flour",
                    quantity = "2",
                    unit = "cups",
                    orderIndex = 0
                ),
                RecipeCreationIngredientUiModel(
                    id = "2",
                    name = "Sugar",
                    quantity = "1",
                    unit = "cup",
                    orderIndex = 1
                )
            ).toImmutableList(),
            onAddSectionClick = { /* no-op */ },
            onEditIngredientClick = { /* no-op */ },
            onAddMainIngredientClick = { /* no-op */ }
        )
    }
}
