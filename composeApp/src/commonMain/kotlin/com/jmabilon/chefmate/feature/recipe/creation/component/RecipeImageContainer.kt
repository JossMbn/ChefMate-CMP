package com.jmabilon.chefmate.feature.recipe.creation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_a_photo_rounded_outlined
import chefmate.composeapp.generated.resources.ic_delete_forever_rounded_outlined
import coil3.compose.AsyncImage
import com.jmabilon.chefmate.designsystem.extension.customClickable
import com.jmabilon.chefmate.designsystem.extension.dashedBorder
import com.jmabilon.chefmate.designsystem.provider.toComposeImageBitmap
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.ImageSource
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeImageContainer(
    modifier: Modifier = Modifier,
    imageSource: ImageSource?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(MaterialTheme.shapes.large)
            .customClickable(onClick = onEditClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageSource != null) {
            when (imageSource) {
                is ImageSource.ByteArray -> {
                    val imageImageBitmap = remember(imageSource.bytes) {
                        imageSource.bytes.toComposeImageBitmap()
                    }

                    Image(
                        modifier = Modifier
                            .matchParentSize(),
                        bitmap = imageImageBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                is ImageSource.Url -> {
                    AsyncImage(
                        modifier = Modifier
                            .matchParentSize(),
                        model = imageSource.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp),
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete_forever_rounded_outlined),
                    contentDescription = null
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .dashedBorder(
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.large,
                        strokeWidth = 6.dp,
                        dashLength = 6.dp,
                        gapLength = 14.dp
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(10.dp),
                    painter = painterResource(Res.drawable.ic_add_a_photo_rounded_outlined),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Add a photo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tap to upload a mouth-watering shot",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecipeImageContainerPreview() {
    ChefMateTheme {
        RecipeImageContainer(
            modifier = Modifier.padding(10.dp),
            imageSource = null,
            onEditClick = { /* no-op */ },
            onDeleteClick = { /* no-op */ }
        )
    }
}
