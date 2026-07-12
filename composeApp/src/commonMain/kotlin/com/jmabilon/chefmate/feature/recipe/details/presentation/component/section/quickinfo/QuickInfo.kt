package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.quickinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.QuickInfoUiModel

@Composable
fun QuickInfo(
    modifier: Modifier = Modifier,
    quickInfo: QuickInfoUiModel
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickInfoItem(
                modifier = Modifier.weight(1f),
                title = "PREP",
                value = quickInfo.prepTime.asStringComposable()
            )

            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            QuickInfoItem(
                modifier = Modifier.weight(1f),
                title = "COOK",
                value = quickInfo.cookTime.asStringComposable()
            )

            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            QuickInfoItem(
                modifier = Modifier.weight(1f),
                title = "DIFFICULTY",
                value = quickInfo.difficulty.asStringComposable()
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickInfoPreview() {
    ChefMateTheme {
        QuickInfo(
            quickInfo = QuickInfoUiModel(
                prepTime = UiText.DynamicString("10 min"),
                cookTime = UiText.DynamicString("20 min"),
                difficulty = UiText.DynamicString("Easy")
            )
        )
    }
}
