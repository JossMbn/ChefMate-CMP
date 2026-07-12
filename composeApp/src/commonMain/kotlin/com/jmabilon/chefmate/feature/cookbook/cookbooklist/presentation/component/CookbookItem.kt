package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.recipe_count
import com.jmabilon.chefmate.core.designsystem.component.recipe.RecipeImageWithPlaceHolder
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun CookbookItem(
    modifier: Modifier = Modifier,
    imageUrl: ImmutableList<String>,
    name: String,
    recipeCount: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(min = 160.dp, max = 200.dp)
            .heightIn(max = 250.dp)
            .customClickable(rippleEnabled = false, onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CookbookImages(
            imageUrl = imageUrl,
            onClick = onClick
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
private fun CookbookImages(
    modifier: Modifier = Modifier,
    imageUrl: ImmutableList<String>,
    onClick: () -> Unit
) {
    val firstImage = remember(imageUrl) { imageUrl.getOrNull(0) }
    val secondImage = remember(imageUrl) { imageUrl.getOrNull(1) }
    val thirdImage = remember(imageUrl) { imageUrl.getOrNull(2) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(MaterialTheme.shapes.large)
            .customClickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        RecipeImageWithPlaceHolder(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight(),
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
                    .fillMaxWidth(),
                imageUrl = secondImage,
                withLogo = false
            )


            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                imageUrl = thirdImage,
                withLogo = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CookbookItemPreview() {
    ChefMateTheme {
        CookbookItem(
            modifier = Modifier.padding(10.dp),
            imageUrl = persistentListOf(),
            name = "My Cookbook",
            recipeCount = 15,
            onClick = { /* no-op */ }
        )
    }
}
