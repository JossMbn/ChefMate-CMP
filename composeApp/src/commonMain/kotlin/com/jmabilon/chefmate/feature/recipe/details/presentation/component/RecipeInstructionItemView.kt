package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.extension.conditional
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.StepUiModel

@Composable
fun RecipeInstructionItemView(
    modifier: Modifier = Modifier,
    step: StepUiModel,
    isLastStep: Boolean = false
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.number,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            if (!isLastStep) {
                VerticalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .conditional(condition = !isLastStep, ifTrue = { padding(bottom = 16.dp) }),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            step.title.takeIf { !it.isNullOrEmpty() }?.let { title ->
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                modifier = Modifier.conditional(
                    condition = step.title.isNullOrEmpty(),
                    ifTrue = { padding(top = 8.dp) }
                ),
                text = step.instruction,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeInstructionItemViewPreview() {
    ChefMateTheme {
        RecipeInstructionItemView(
            modifier = Modifier.padding(10.dp),
            step = StepUiModel(
                number = "1",
                title = "Prep the oven",
                instruction = "Preheat the oven to 350°F (175°C). Grease and flour a 9x9 inch pan."
            )
        )
    }
}
