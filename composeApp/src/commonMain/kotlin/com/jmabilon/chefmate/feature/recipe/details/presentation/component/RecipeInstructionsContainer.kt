package com.jmabilon.chefmate.feature.recipe.details.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionsUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.StepUiModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecipeInstructionContainer(
    modifier: Modifier = Modifier,
    instructions: InstructionsUiModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            instructions.steps.forEach { step ->
                RecipeInstructionItemView(
                    step = step,
                    isLastStep = step == instructions.steps.last()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeInstructionContainerPreview() {
    ChefMateTheme {
        RecipeInstructionContainer(
            instructions = InstructionsUiModel(
                steps = persistentListOf(
                    StepUiModel(
                        number = "1",
                        title = "Prep the oven",
                        instruction = "Preheat the oven to 350°F (175°C). Grease and"
                    ),
                    StepUiModel(
                        number = "2",
                        title = "Mix the ingredients",
                        instruction = "In a large bowl, combine flour, sugar, and eggs. Mix until smooth."
                    ),
                    StepUiModel(
                        number = "3",
                        title = "Bake the cake",
                        instruction = "Pour the batter into a greased baking pan and bake for 30-35 minutes, or until a toothpick inserted into the center comes out clean."
                    )
                )
            )
        )
    }
}
