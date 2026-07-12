package com.jmabilon.chefmate.feature.recipe.details.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.details.presentation.RecipeDetailsState
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientInfo
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientSectionUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.QuickInfoUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeUiModel
import kotlinx.collections.immutable.persistentListOf

class RecipeDetailsPagePreviewProvider : PreviewParameterProvider<RecipeDetailsState> {

    // =============================================================================================
    //  Sequence
    // =============================================================================================

    override val values: Sequence<RecipeDetailsState>
        get() = sequenceOf(
            loadingState,
            contentState
        )

    // =============================================================================================
    //  States
    // =============================================================================================

    private val loadingState = RecipeDetailsState(
        recipe = AsyncState.Loading
    )

    private val contentState = RecipeDetailsState(
        recipe = AsyncState.Content(
            data = RecipeUiModel(
                id = "1",
                name = "Spaghetti Bolognese",
                imageUrl = "https://example.com/spaghetti.jpg",
                quickInfo = QuickInfoUiModel(
                    prepTime = UiText.DynamicString("30 mins"),
                    cookTime = UiText.DynamicString("1 hr"),
                    difficulty = UiText.DynamicString("Medium")
                ),
                serving = "4",
                ingredients = persistentListOf(
                    IngredientSectionUiModel(
                        title = "Main",
                        ingredients = persistentListOf(
                            IngredientInfo(name = "Spaghetti", quantityUnit = "400g"),
                            IngredientInfo(name = "Ground Beef", quantityUnit = "500g"),
                            IngredientInfo(name = "Onion", quantityUnit = "1, chopped"),
                            IngredientInfo(name = "Garlic", quantityUnit = "2 cloves, minced"),
                            IngredientInfo(name = "Tomato Sauce", quantityUnit = "400ml")
                        )
                    ),
                    IngredientSectionUiModel(
                        title = "Seasonings",
                        ingredients = persistentListOf(
                            IngredientInfo(name = "Salt", quantityUnit = "to taste"),
                            IngredientInfo(name = "Black Pepper", quantityUnit = "to taste"),
                            IngredientInfo(name = "Olive Oil", quantityUnit = "2 tbsp")
                        )
                    )
                ),
                instructions = persistentListOf(
                    InstructionUiModel(
                        index = "1",
                        instruction = "Cook the spaghetti according to package instructions."
                    ),
                    InstructionUiModel(
                        index = "2",
                        instruction = "In a large pan, heat olive oil and sauté onions and garlic until translucent."
                    ),
                    InstructionUiModel(
                        index = "3",
                        instruction = "Add ground beef and cook until browned."
                    )
                )
            )
        )
    )
}
