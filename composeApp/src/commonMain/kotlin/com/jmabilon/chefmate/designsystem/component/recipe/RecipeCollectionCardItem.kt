package com.jmabilon.chefmate.designsystem.component.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_count
import com.jmabilon.chefmate.designsystem.extension.customClickable
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun RecipeCollectionCardItem(
    modifier: Modifier = Modifier,
    imageUrl: ImmutableList<String>,
    name: String,
    recipeCount: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(min = 160.dp, max = 200.dp)
            .heightIn(min = 200.dp, max = 250.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .customClickable(onClick = onClick)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RecipeCollectionImages(imageUrl = imageUrl)

        Column(
            modifier = Modifier.padding(horizontal = 6.dp).padding(bottom = 2.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = pluralStringResource(Res.plurals.recipe_count, recipeCount, recipeCount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecipeCollectionImages(
    modifier: Modifier = Modifier,
    imageUrl: ImmutableList<String>
) {
    val firstImage = remember(imageUrl) { imageUrl.getOrNull(0) }
    val secondImage = remember(imageUrl) { imageUrl.getOrNull(1) }
    val thirdImage = remember(imageUrl) { imageUrl.getOrNull(2) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        RecipeImageWithPlaceHolder(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.extraSmall),
            imageUrl = firstImage,
            withLogo = false
        )

        Column(
            modifier = Modifier
                .weight(4f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraSmall),
                imageUrl = secondImage,
                withLogo = false
            )


            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraSmall),
                imageUrl = thirdImage,
                withLogo = false
            )
        }
    }
}

@Preview
@Composable
private fun RecipeCollectionCardItemPreview() {
    ChefMateTheme {
        RecipeCollectionCardItem(
            modifier = Modifier.padding(10.dp),
            imageUrl = persistentListOf(),
            name = "My Collection",
            recipeCount = 15,
            onClick = { /* no-op */ }
        )
    }
}
