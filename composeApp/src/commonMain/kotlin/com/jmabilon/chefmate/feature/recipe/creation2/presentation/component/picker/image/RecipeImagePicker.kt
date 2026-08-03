package com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jmabilon.chefmate.core.designsystem.extension.customClickable
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image.placeholder.RecipeImagePlaceholder
import kotlinx.io.bytestring.ByteString

@Stable
sealed interface RecipeImage {

    data object None : RecipeImage

    data class Local(val bytes: ByteString) : RecipeImage

    data class Remote(val url: String) : RecipeImage
}

@Composable
fun RecipeImagePicker(
    modifier: Modifier = Modifier,
    image: RecipeImage,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .customClickable(onClick = onClick)
    ) {
        when (image) {
            is RecipeImage.None -> {
                RecipeImagePlaceholder(
                    modifier = Modifier.matchParentSize()
                )
            }

            is RecipeImage.Local -> {
                val model = remember(image.bytes) {
                    image.bytes.toByteArray()
                }

                AsyncImage(
                    modifier = Modifier.matchParentSize(),
                    model = model,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }

            is RecipeImage.Remote -> {
                AsyncImage(
                    modifier = Modifier.matchParentSize(),
                    model = image.url,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeImagePickerPreview() {
    ChefMateTheme {
        RecipeImagePicker(
            image = RecipeImage.None,
            onClick = { /* no-op */ }
        )
    }
}
