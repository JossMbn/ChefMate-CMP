package com.jmabilon.chefmate.feature.recipe.details.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jmabilon.chefmate.core.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.details.presentation.RecipeDetailsState
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.DifficultyInfoUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientGroupUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientItemUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.IngredientsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeDetailsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.StepUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.TimeInfoUiModel
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
        loadingContentState = LoadingContentState.Loading
    )

    private val contentState = RecipeDetailsState(
        loadingContentState = LoadingContentState.Content,
        recipeDetails = RecipeDetailsUiModel(
            id = "recipe-001",
            title = "Chocolate Chip Cookies",
            imageUrl = null,
            timeInfo = TimeInfoUiModel(
                prepTimeText = UiText.DynamicString("15 min"),
                cookTimeText = UiText.DynamicString("12 min")
            ),
            difficultyInfo = DifficultyInfoUiModel(
                difficulty = UiText.DynamicString("Easy")
            ),
            ingredients = IngredientsUiModel(
                servings = 4,
                groups = persistentListOf(
                    IngredientGroupUiModel(
                        title = null,
                        items = persistentListOf(
                            IngredientItemUiModel(
                                id = "ing-1",
                                baseQuantity = 2.0,
                                currentQuantity = 2.0,
                                unit = "cups",
                                ingredientDisplayText = "Flour"
                            ),
                            IngredientItemUiModel(
                                id = "ing-2",
                                baseQuantity = 1.0,
                                currentQuantity = 1.0,
                                unit = "cup",
                                ingredientDisplayText = "Sugar"
                            ),
                            IngredientItemUiModel(
                                id = "ing-3",
                                baseQuantity = 0.5,
                                currentQuantity = 0.5,
                                unit = "cup",
                                ingredientDisplayText = "Butter"
                            )
                        )
                    ),
                    IngredientGroupUiModel(
                        title = "For the sauce",
                        items = persistentListOf(
                            IngredientItemUiModel(
                                id = "ing-5",
                                baseQuantity = 1.0,
                                currentQuantity = 1.0,
                                unit = "cup",
                                ingredientDisplayText = "Tomato sauce"
                            ),
                            IngredientItemUiModel(
                                id = "ing-6",
                                baseQuantity = 2.0,
                                currentQuantity = 2.0,
                                unit = "tbsp",
                                ingredientDisplayText = "Olive oil"
                            )
                        )
                    )
                )
            ),
            instructions = InstructionsUiModel(
                steps = persistentListOf(
                    StepUiModel(
                        number = "1",
                        title = "Mix dry ingredients",
                        instruction = "Combine flour with a pinch of salt in a bowl."
                    ),
                    StepUiModel(
                        number = "2",
                        title = "Cream butter and sugar",
                        instruction = "Beat butter and sugar together until light and fluffy."
                    ),
                    StepUiModel(
                        number = "3",
                        title = "Combine and add chocolate chips",
                        instruction = "Gradually add the dry ingredients to the butter mixture, then fold in the chocolate chips."
                    ),
                    StepUiModel(
                        number = "4",
                        title = "Bake at 180°C",
                        instruction = "Bake the cookies in a preheated oven at 180°C for 10-12 minutes, or until golden brown."
                    )
                )
            )
        )
    )
}
