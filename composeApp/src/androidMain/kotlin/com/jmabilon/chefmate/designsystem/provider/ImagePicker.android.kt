package com.jmabilon.chefmate.designsystem.provider

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.collections.immutable.ImmutableList

@Composable
actual fun rememberImagePicker(onImagePicked: (List<Byte>?) -> Unit): ImagePicker {
    val context = LocalContext.current

    // On prépare le launcher Android standard
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            onImagePicked(null)
            return@rememberLauncherForActivityResult
        }

        // Lecture du flux de données depuis l'URI
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()?.toList()
        inputStream?.close()
        onImagePicked(bytes)
    }

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                // Lancement du Photo Picker
                launcher.launch(
                    input = PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    }
}

actual fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this.toByteArray(), 0, size).asImageBitmap()
}
