package com.jmabilon.chefmate.feature.recipe.details.model.recipe

import com.jmabilon.chefmate.core.presentation.extension.formatQuantity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class IngredientsUiModel(
    val servings: Int = 1,
    val groups: ImmutableList<IngredientGroupUiModel> = persistentListOf()
)

data class IngredientGroupUiModel(
    val title: String?, // "Pour la pâte", "Pour la garniture"
    val items: ImmutableList<IngredientItemUiModel>
)

data class IngredientItemUiModel(
    val id: String,
    val baseQuantity: Double?,
    val currentQuantity: Double?,
    val unit: String?,
    val ingredientDisplayText: String, // "Flour", "Sugar", "Butter"
) {
    val quantityUnitDisplayText: String?
        get() {
            val currentQuantityFormatted = currentQuantity?.formatQuantity()

            return when {
                currentQuantity != null && unit != null -> "$currentQuantityFormatted $unit"
                currentQuantity != null && unit == null -> currentQuantityFormatted.toString()
                else -> null
            }
        }
}
