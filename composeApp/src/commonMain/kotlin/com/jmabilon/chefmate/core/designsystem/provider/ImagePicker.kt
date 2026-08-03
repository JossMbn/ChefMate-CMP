package com.jmabilon.chefmate.core.designsystem.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.io.bytestring.ByteString

interface ImagePicker {
    fun pickImage()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (ByteString) -> Unit): ImagePicker

expect fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap
