package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.time.TimeItem
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.model.TimeUiModel

@Composable
fun BaseInfoSection(
    modifier: Modifier = Modifier,
    prepTime: TimeUiModel,
    cookTime: TimeUiModel,
    serves: String,
    onPrepTimeValueChange: (Int, Int) -> Unit,
    onCookTimeValueChange: (Int, Int) -> Unit,
    onDecreaseServesClick: () -> Unit,
    onIncreaseServesClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeItem(
            modifier = Modifier.weight(1f),
            label = "Prep Time",
            model = prepTime,
            onValueChange = onPrepTimeValueChange
        )

        TimeItem(
            modifier = Modifier.weight(1f),
            label = "Cook Time",
            model = cookTime,
            onValueChange = onCookTimeValueChange
        )

        ServeSelector(
            modifier = Modifier.weight(1f),
            serves = serves,
            onDecreaseClick = onDecreaseServesClick,
            onIncreaseClick = onIncreaseServesClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseInfoSectionPreview() {
    ChefMateTheme {
        BaseInfoSection(
            prepTime = TimeUiModel(
                time = UiText.DynamicString("15 min"),
                hour = 0,
                minute = 15,
            ),
            cookTime = TimeUiModel(
                time = UiText.DynamicString("30 min"),
                hour = 0,
                minute = 30,
            ),
            serves = "4",
            onPrepTimeValueChange = { _, _ -> /* no-op */ },
            onCookTimeValueChange = { _, _ -> /* no-op */ },
            onDecreaseServesClick = { /* no-op */ },
            onIncreaseServesClick = { /* no-op */ }
        )
    }
}
