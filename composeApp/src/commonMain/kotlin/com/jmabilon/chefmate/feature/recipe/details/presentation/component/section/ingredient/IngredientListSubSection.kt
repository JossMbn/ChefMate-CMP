package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ingredient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun IngredientListSubSection(
    modifier: Modifier = Modifier,
    name: String,
    ingredients: ImmutableList<IngredientInfo>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Column {
            ingredients.forEach { ingredient ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = ingredient.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    AnimatedContent(
                        targetState = ingredient.quantityUnit,
                        transitionSpec = {
                            fadeIn(tween(durationMillis = 600)) togetherWith fadeOut(tween(durationMillis = 600))
                        },
                        label = "Ingredient Quantity Unit"
                    ) { targetQuantityUnit ->
                        Text(
                            text = targetQuantityUnit,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if (ingredient != ingredients.last()) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun IngredientListSubSectionPreview() {
    ChefMateTheme {
        IngredientListSubSection(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant),
            name = "MAIN",
            ingredients = persistentListOf(
                IngredientInfo(name = "Chicken", quantityUnit = "200 g"),
                IngredientInfo(name = "Salt", quantityUnit = "1 tsp"),
                IngredientInfo(name = "Pepper", quantityUnit = "1/2 tsp"),
                IngredientInfo(name = "Olive Oil", quantityUnit = "2 tbsp"),
                IngredientInfo(name = "Heavy cream", quantityUnit = "100 ml")
            )
        )
    }
}
