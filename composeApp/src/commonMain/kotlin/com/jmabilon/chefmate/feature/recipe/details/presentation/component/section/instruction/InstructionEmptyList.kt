package com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.instruction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_notes_rounded_fill
import com.jmabilon.chefmate.core.designsystem.extension.dashedBorder
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.PrimaryButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun InstructionEmptyList(
    modifier: Modifier = Modifier,
    onAddInstructionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .dashedBorder(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(vertical = 26.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(Res.drawable.ic_notes_rounded_fill),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No steps yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Write the steps one by one — you'll follow them hands-free in Cook mode.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
        }

        PrimaryButton(
            modifier = Modifier.padding(top = 10.dp),
            label = "Add an instruction",
            onClick = onAddInstructionClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InstructionEmptyListPreview() {
    ChefMateTheme {
        InstructionEmptyList(
            modifier = Modifier.padding(10.dp),
            onAddInstructionClick = { /* no-op */ }
        )
    }
}
