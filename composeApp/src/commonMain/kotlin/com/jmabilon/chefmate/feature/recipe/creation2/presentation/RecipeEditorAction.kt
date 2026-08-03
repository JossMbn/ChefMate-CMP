package com.jmabilon.chefmate.feature.recipe.creation2.presentation

import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.RecipeEditorDifficultyUiModel
import kotlinx.io.bytestring.ByteString

sealed interface RecipeEditorAction {

    data class OnImagePicked(val bytes: ByteString) : RecipeEditorAction

    data class OnTitleChanged(val title: String) : RecipeEditorAction

    data class OnPrepTimeChanged(val hour: Int, val minute: Int) : RecipeEditorAction

    data class OnCookTimeChanged(val hour: Int, val minute: Int) : RecipeEditorAction

    data object OnDecreaseServesClicked : RecipeEditorAction

    data object OnIncreaseServesClicked : RecipeEditorAction

    data class OnDifficultyChanged(val difficulty: RecipeEditorDifficultyUiModel) : RecipeEditorAction
}
