package com.jmabilon.chefmate.core.designsystem.newcomponent.recipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jmabilon.chefmate.core.designsystem.component.recipe.RecipeImageWithPlaceHolder
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.provider.compositionlocal.RemoveDefaultPaddingProvider
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.UiText
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeCard(
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
            .widthIn(min = 160.dp, max = 200.dp)
            .heightIn(max = 250.dp)
            .customClickable(rippleEnabled = false, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(MaterialTheme.shapes.large)
                    .customClickable(onClick = onClick),
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

        RemoveDefaultPaddingProvider {
            IconButton(
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = (-8).dp, y = 8.dp)
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                onClick = { /* no-op */ }
            ) {
                AnimatedContent(
                    modifier = Modifier,
                    targetState = favoriteIcon,
                    contentAlignment = Alignment.Center
                ) { targetState ->
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .customClickable(rippleEnabled = false, onClick = onFavoriteClick),
                        painter = painterResource(targetState),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
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
private fun RecipeCardPreview() {
    ChefMateTheme {
        RecipeCard(
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
