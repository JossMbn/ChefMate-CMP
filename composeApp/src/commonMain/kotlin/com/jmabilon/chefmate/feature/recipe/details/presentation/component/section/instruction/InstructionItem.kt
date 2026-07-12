package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.instruction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme

@Composable
fun InstructionItem(
    modifier: Modifier = Modifier,
    instruction: String,
    instructionIndex: String
) {
    var isInstructionMultiLine by remember { mutableStateOf(false) }

    val verticalAlignment by derivedStateOf {
        if (isInstructionMultiLine) Alignment.Top else Alignment.CenterVertically
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = verticalAlignment
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = instructionIndex,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = instruction,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            onTextLayout = { textLayoutResult ->
                isInstructionMultiLine = textLayoutResult.lineCount > 1
            }
        )
    }
}

@Preview
@Composable
private fun InstructionItemPreview() {
    ChefMateTheme {
        InstructionItem(
            instruction = "Preheat the oven to 350°F (175°C).",
            instructionIndex = "1"
        )
    }
}
