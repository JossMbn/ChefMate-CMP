package com.jmabilon.chefmate.designsystem.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList

interface ImagePicker {
    fun pickImage()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (List<Byte>?) -> Unit): ImagePicker

expect fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap
