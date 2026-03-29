package com.jmabilon.chefmate.feature.recipe.details.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.model.recipe.IngredientItemUiModel

@Composable
fun IngredientText(
    modifier: Modifier = Modifier,
    ingredient: IngredientItemUiModel
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        text = buildAnnotatedString {
            append("• ")

            withStyle(
                style = MaterialTheme.typography.bodyMedium
                    .toSpanStyle()
                    .copy(fontWeight = FontWeight.Bold)
            ) {
                append(ingredient.quantityUnitDisplayText)
            }

            if (!ingredient.quantityUnitDisplayText.isNullOrBlank()) {
                append(" ")
                append(ingredient.ingredientDisplayText)
            }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
private fun IngredientTextPreview() {
    ChefMateTheme {
        IngredientText(
            ingredient = IngredientItemUiModel(
                id = "1",
                baseQuantity = 1.0,
                currentQuantity = 1.0,
                unit = "cup",
                ingredientDisplayText = "Flour"
            )
        )
    }
}
