package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientGroupUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientItemUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionUiModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecipeIngredientMainContainer(
    modifier: Modifier = Modifier,
    servings: Int,
    group: IngredientGroupUiModel,
    onServingsChanged: (RecipeServingActionUiModel) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.headlineSmall
            )

            ServingsSelector(
                currentServings = servings,
                onServingsChanged = onServingsChanged
            )
        }

        group.items.forEach { item ->
            IngredientText(ingredient = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeIngredientMainContainerPreview() {
    ChefMateTheme {
        RecipeIngredientMainContainer(
            servings = 2,
            group = IngredientGroupUiModel(
                title = "Main",
                items = persistentListOf(
                    IngredientItemUiModel(
                        id = "1",
                        baseQuantity = 1.0,
                        currentQuantity = 1.0,
                        unit = "cup",
                        ingredientDisplayText = "Flour"
                    ),
                    IngredientItemUiModel(
                        id = "2",
                        baseQuantity = 2.0,
                        currentQuantity = 2.0,
                        unit = "tbsp",
                        ingredientDisplayText = "Sugar"
                    ),
                    IngredientItemUiModel(
                        id = "3",
                        baseQuantity = 1.0,
                        currentQuantity = 1.0,
                        unit = "tsp",
                        ingredientDisplayText = "Salt"
                    )
                )
            ),
            onServingsChanged = { /* no-op */ }
        )
    }
}
