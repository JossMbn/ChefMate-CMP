package com.jmabilon.chefmate.feature.recipe.creation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.feature.recipe.creation.model.RecipeIngredientUiData
import kotlinx.collections.immutable.ImmutableList

@Composable
fun IngredientListContainer(
    modifier: Modifier = Modifier,
    ingredients: ImmutableList<RecipeIngredientUiData>,
    onEditIngredientClick: (ingredientId: String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ingredients.forEach { ingredient ->
            IngredientItem(
                ingredient = ingredient,
                onEditClick = { onEditIngredientClick(ingredient.id) }
            )
        }
    }
}
