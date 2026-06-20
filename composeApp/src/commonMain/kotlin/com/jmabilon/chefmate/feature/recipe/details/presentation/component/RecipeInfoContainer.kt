package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_equalizer_rounded_fill
import chefmate.composeapp.generated.resources.ic_local_fire_department_rounded_fill
import chefmate.composeapp.generated.resources.ic_schedule_rounded_fill
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.DifficultyInfoUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.TimeInfoUiModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeInfoContainer(
    modifier: Modifier = Modifier,
    timeInfo: TimeInfoUiModel,
    difficultyInfo: DifficultyInfoUiModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        timeInfo.prepTimeText?.let { prepTime ->
            RecipeInfoCard(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.ic_schedule_rounded_fill),
                value = prepTime.asStringComposable(),
                title = "Prep"
            )
        }
        timeInfo.cookTimeText?.let { cookTime ->
            RecipeInfoCard(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.ic_local_fire_department_rounded_fill),
                value = cookTime.asStringComposable(),
                title = "Cook"
            )
        }

        difficultyInfo.difficulty?.let { difficulty ->
            RecipeInfoCard(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.ic_equalizer_rounded_fill),
                value = difficulty.asStringComposable(),
                title = "Level"
            )
        }
    }
}

@Preview
@Composable
private fun RecipeInfoContainerPreview() {
    ChefMateTheme {
        RecipeInfoContainer(
            timeInfo = TimeInfoUiModel(
                prepTimeText = UiText.DynamicString("15 min"),
                cookTimeText = UiText.DynamicString("30 min")
            ),
            difficultyInfo = DifficultyInfoUiModel(
                difficulty = UiText.DynamicString("Easy")
            )
        )
    }
}
