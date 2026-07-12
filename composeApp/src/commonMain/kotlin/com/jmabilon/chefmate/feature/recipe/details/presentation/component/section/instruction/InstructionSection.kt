package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.instruction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ingredient.SectionTitleHeader
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.InstructionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun InstructionsSection(
    modifier: Modifier = Modifier,
    instructions: ImmutableList<InstructionUiModel>,
    onAddInstructionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionTitleHeader(
            modifier = Modifier.fillMaxWidth(),
            title = "Instructions"
        )

        if (instructions.isEmpty()) {
            InstructionEmptyList(
                modifier = Modifier.fillMaxWidth(),
                onAddInstructionClick = onAddInstructionClick
            )
        } else {
            instructions.forEach { instruction ->
                InstructionItem(
                    modifier = Modifier.fillMaxWidth(),
                    instruction = instruction.instruction,
                    instructionIndex = instruction.index
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InstructionsSectionPreview() {
    ChefMateTheme {
        InstructionsSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            instructions = persistentListOf(
                InstructionUiModel(
                    index = "1",
                    instruction = "Preheat the oven to 350°F (175°C)."
                ),
                InstructionUiModel(
                    index = "2",
                    instruction = "In a large bowl, mix the flour, sugar, and eggs until smooth."
                ),
                InstructionUiModel(
                    index = "3",
                    instruction = "Pour the batter into a greased baking pan and bake for 30 minutes."
                )
            ),
            onAddInstructionClick = { /* no-op */ }
        )
    }
}
