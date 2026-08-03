package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image.placeholder

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_a_photo_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.extension.dashedBorder
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeImagePlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .dashedBorder(
                color = MaterialTheme.colorScheme.outlineVariant,
                strokeWidth = 1.dp,
                shape = MaterialTheme.shapes.extraLarge,
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(26.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp),
            painter = painterResource(Res.drawable.ic_add_a_photo_rounded_outlined),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Add cover photo",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Tap to upload a mouth-watering shot",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeImagePlaceholderPreview() {
    ChefMateTheme {
        RecipeImagePlaceholder(
            modifier = Modifier.padding(16.dp)
        )
    }
}
