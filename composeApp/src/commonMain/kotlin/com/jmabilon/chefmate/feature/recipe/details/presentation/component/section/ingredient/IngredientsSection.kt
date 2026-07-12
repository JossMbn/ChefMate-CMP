package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientInfo
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientSectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Composable
fun IngredientsSection(
    modifier: Modifier = Modifier,
    servings: String,
    ingredientSections: ImmutableList<IngredientSectionUiModel>,
    onServingDecreased: () -> Unit,
    onServingIncreased: () -> Unit,
    onAddIngredientClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionTitleHeader(
            modifier = Modifier.fillMaxWidth(),
            title = "Ingredients"
        ) {
            if (ingredientSections.isNotEmpty()) {
                ServingStepper(
                    serving = servings,
                    onRemoveClick = onServingDecreased,
                    onAddClick = onServingIncreased
                )
            }
        }

        if (ingredientSections.isEmpty()) {
            IngredientEmptyList(
                modifier = Modifier.fillMaxWidth(),
                onAddIngredientClick = onAddIngredientClick
            )
        } else {
            ingredientSections.forEach { section ->
                IngredientListSubSection(
                    modifier = Modifier.fillMaxWidth(),
                    name = section.title,
                    ingredients = section.ingredients
                )
            }
        }
    }
}

@Preview
@Composable
private fun IngredientsSectionPreview() {
    ChefMateTheme {
        IngredientsSection(
            servings = "2",
            ingredientSections = persistentListOf(
                IngredientSectionUiModel(
                    title = "MAIN",
                    ingredients = persistentListOf(
                        IngredientInfo(name = "Chicken", quantityUnit = "200g"),
                        IngredientInfo(name = "Salt", quantityUnit = "1 tsp"),
                        IngredientInfo(name = "Pepper", quantityUnit = "1/2 tsp")
                    )
                ),
                IngredientSectionUiModel(
                    title = "SAUCE",
                    ingredients = persistentListOf(
                        IngredientInfo(name = "Olive Oil", quantityUnit = "2 tbsp"),
                        IngredientInfo(name = "Garlic", quantityUnit = "2 cloves"),
                        IngredientInfo(name = "Lemon Juice", quantityUnit = "1 tbsp")
                    )
                )
            ),
            onServingDecreased = { /* no-op */ },
            onServingIncreased = { /* no-op */ },
            onAddIngredientClick = { /* no-op */ }
        )
    }
}
