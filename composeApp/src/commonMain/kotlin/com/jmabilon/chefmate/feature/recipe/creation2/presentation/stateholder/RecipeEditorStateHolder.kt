package com.jmabilon.chefmate.feature.recipe.creation2.presentation.stateholder

import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.formatDuration
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image.RecipeImage
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.RecipeEditorDifficultyUiModel
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.RecipeEditorUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime
import kotlinx.io.bytestring.ByteString

class RecipeEditorStateHolder {

    // =================================================================================================
    // Properties
    // =================================================================================================

    private val _recipe = MutableStateFlow(RecipeEditorUiModel())
    val recipe = _recipe.asStateFlow()

    // =================================================================================================
    // Public Methods
    // =================================================================================================

    fun onImageChanged(bytes: ByteString) {
        _recipe.update { currentState ->
            currentState.copy(
                image = RecipeImage.Local(bytes)
            )
        }
    }

    fun onTitleChanged(title: String) {
        _recipe.update { currentState ->
            currentState.copy(
                title = title
            )
        }
    }

    fun onPrepTimeChanged(hour: Int, minute: Int) {
        val prepTimeUiText = formatDuration(hour, minute)

        _recipe.update { currentState ->
            currentState.copy(
                time = currentState.time.copy(
                    prepTime = currentState.time.prepTime.copy(
                        time = prepTimeUiText,
                        hour = hour,
                        minute = minute
                    )
                )
            )
        }
    }

    fun onCookTimeChanged(hour: Int, minute: Int) {
        val prepTimeUiText = formatDuration(hour, minute)

        _recipe.update { currentState ->
            currentState.copy(
                time = currentState.time.copy(
                    cookTime = currentState.time.cookTime.copy(
                        time = prepTimeUiText,
                        hour = hour,
                        minute = minute
                    )
                )
            )
        }
    }

    fun onDecreaseServes() {
        _recipe.update { currentState ->
            val newServes = (currentState.serves.toInt() - 1).coerceAtLeast(1)

            currentState.copy(
                serves = newServes.toString()
            )
        }
    }

    fun onIncreaseServes() {
        _recipe.update { currentState ->
            val newServes = (currentState.serves.toInt() + 1).coerceAtMost(100)

            currentState.copy(
                serves = newServes.toString()
            )
        }
    }

    fun onDifficultyChanged(difficulty: RecipeEditorDifficultyUiModel) {
        _recipe.update { currentState ->
            currentState.copy(
                difficulty = difficulty
            )
        }
    }

    // =================================================================================================
    // Private Methods
    // =================================================================================================

    fun formatDuration(hour: Int, minute: Int): UiText {
        val timeInSecond = LocalTime(hour, minute).toSecondOfDay()
        return timeInSecond.formatDuration() ?: UiText.DynamicString("—")
    }
}
