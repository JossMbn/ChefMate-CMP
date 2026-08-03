package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_easy_level
import chefmate.composeapp.generated.resources.recipe_hard_level
import chefmate.composeapp.generated.resources.recipe_medium_level
import com.jmabilon.chefmate.core.designsystem.newcomponent.common.FieldLabelContainer
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.difficulty.item.DifficultyItem
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.RecipeEditorDifficultyUiModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun DifficultySection(
    modifier: Modifier = Modifier,
    difficulty: RecipeEditorDifficultyUiModel?,
    onClick: (RecipeEditorDifficultyUiModel) -> Unit
) {
    FieldLabelContainer(
        label = "Difficulty"
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DifficultyItem(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(Res.string.recipe_easy_level),
                selected = difficulty == RecipeEditorDifficultyUiModel.Easy,
                onClick = { onClick(RecipeEditorDifficultyUiModel.Easy) },
            )

            DifficultyItem(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(Res.string.recipe_medium_level),
                selected = difficulty == RecipeEditorDifficultyUiModel.Medium,
                onClick = { onClick(RecipeEditorDifficultyUiModel.Medium) },
            )

            DifficultyItem(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(Res.string.recipe_hard_level),
                selected = difficulty == RecipeEditorDifficultyUiModel.Hard,
                onClick = { onClick(RecipeEditorDifficultyUiModel.Hard) },
            )
        }
    }
}

@Preview
@Composable
private fun DifficultySectionPreview() {
    DifficultySection(
        difficulty = RecipeEditorDifficultyUiModel.Easy,
        onClick = { /* no-op */ }
    )
}
