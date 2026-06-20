package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientGroupUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientItemUiModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecipeIngredientSubContainer(
    modifier: Modifier = Modifier,
    group: IngredientGroupUiModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = group.title.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        group.items.forEach { item ->
            IngredientText(ingredient = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeIngredientSubContainerPreview() {
    ChefMateTheme {
        RecipeIngredientSubContainer(
            group = IngredientGroupUiModel(
                title = "For the sauce",
                items = persistentListOf(
                    IngredientItemUiModel(
                        id = "1",
                        baseQuantity = 1.0,
                        currentQuantity = 1.0,
                        unit = "cup",
                        ingredientDisplayText = "Tomato sauce"
                    ),
                    IngredientItemUiModel(
                        id = "2",
                        baseQuantity = 2.0,
                        currentQuantity = 2.0,
                        unit = "tbsp",
                        ingredientDisplayText = "Olive oil"
                    )
                )
            )
        )
    }
}
