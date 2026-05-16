package com.jmabilon.chefmate.designsystem.component.recipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_favorite_rounded_fill
import chefmate.composeapp.generated.resources.ic_favorite_rounded_outlined
import chefmate.composeapp.generated.resources.ic_schedule_rounded_fill
import com.jmabilon.chefmate.designsystem.extension.customClickable
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.designsystem.utils.UiText
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeCardItem(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String?,
    prepTimeMinute: UiText?,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val favoriteIcon = remember(isFavorite) {
        if (isFavorite) {
            Res.drawable.ic_favorite_rounded_fill
        } else {
            Res.drawable.ic_favorite_rounded_outlined
        }
    }

    Box(
        modifier = modifier
            .width(160.dp)
            .customClickable(rippleEnabled = false, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                imageUrl = imageUrl
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
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

                if (prepTimeMinute != null) {
                    RecipeInformationTextIcon(
                        text = prepTimeMinute.asStringComposable(),
                        painter = painterResource(Res.drawable.ic_schedule_rounded_fill)
                    )
                }
            }
        }

        AnimatedContent(
            modifier = Modifier
                .align(Alignment.TopEnd),
            targetState = favoriteIcon,
            contentAlignment = Alignment.Center
        ) { targetState ->
            Icon(
                modifier = Modifier
                    .padding(6.dp)
                    .size(24.dp)
                    .customClickable(rippleEnabled = false, onClick = onFavoriteClick),
                painter = painterResource(targetState),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun RecipeInformationTextIcon(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeCardItemPreview() {
    ChefMateTheme {
        RecipeCardItem(
            modifier = Modifier
                .padding(10.dp),
            name = "Spaghetti Carbonara",
            imageUrl = null,
            prepTimeMinute = UiText.DynamicString("20 min"),
            isFavorite = false,
            onFavoriteClick = { /* no-op */ },
            onClick = { /* no-op */ }
        )
    }
}
