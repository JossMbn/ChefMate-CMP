package com.jmabilon.chefmate.feature.cookbook.selection.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.CookbookSelectionState
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.model.CookbookSelectionUiModel
import kotlinx.collections.immutable.persistentListOf

class CookbookSelectionScreenPreviewProvider : PreviewParameterProvider<CookbookSelectionState> {

    // =================================================================================================
    // Sequences
    // =================================================================================================

    val loadingStateSequence: CookbookSelectionState = CookbookSelectionState()

    val emptyStateSequence: CookbookSelectionState = CookbookSelectionState(cookbooks = AsyncState.Failure)

    val fullStateSequence: CookbookSelectionState = CookbookSelectionState(
        cookbooks = AsyncState.Content(
            persistentListOf(
                CookbookSelectionUiModel(
                    id = "1",
                    imageUrl = null,
                    name = "Dinner Recipes",
                    recipeCount = 10,
                    checked = true
                ),
                CookbookSelectionUiModel(
                    id = "2",
                    imageUrl = null,
                    name = "Dessert Recipes",
                    recipeCount = 3,
                    checked = false
                ),
                CookbookSelectionUiModel(
                    id = "3",
                    imageUrl = null,
                    name = "Healthy Recipes",
                    recipeCount = 24,
                    checked = true
                )
            )
        )
    )

    override val values: Sequence<CookbookSelectionState>
        get() = sequenceOf(
            loadingStateSequence,
            emptyStateSequence,
            fullStateSequence
        )

}
