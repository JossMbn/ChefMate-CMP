package com.jmabilon.chefmate.designsystem.component.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_chef_hat_rounded_fill
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeImageWithPlaceHolder(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    withLogo: Boolean = true
) {
    var isImageStateError by remember { mutableStateOf(false) }

    if (isImageStateError) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (withLogo) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(Res.drawable.ic_chef_hat_rounded_fill),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        AsyncImage(
            modifier = modifier,
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onError = { isImageStateError = true }
        )
    }
}
