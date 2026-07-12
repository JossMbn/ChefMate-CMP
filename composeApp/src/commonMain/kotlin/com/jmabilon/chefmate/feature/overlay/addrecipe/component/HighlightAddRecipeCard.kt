package com.jmabilon.chefmate.feature.overlay.addrecipe.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_apple
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import chefmate.composeapp.generated.resources.ic_google
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun HighlightAddRecipeCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .customClickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_google),
                contentDescription = null
            )

            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_apple),
                contentDescription = null
            )

            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_google),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }

        Icon(
            modifier = Modifier.size(18.dp).rotate(180f),
            painter = painterResource(Res.drawable.ic_arrow_back_rounded_outlined),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

@Preview
@Composable
private fun HighlightAddRecipeCardPreview() {
    ChefMateTheme {
        HighlightAddRecipeCard(
            title = "From scoial media",
            description = "Share to ChefMate from social apps",
            onClick = { /* no-op */ }
        )
    }
}
