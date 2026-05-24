package com.jmabilon.chefmate.core.designsystem.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList

interface ImagePicker {
    fun pickImage()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (List<Byte>?) -> Unit): ImagePicker

expect fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap
