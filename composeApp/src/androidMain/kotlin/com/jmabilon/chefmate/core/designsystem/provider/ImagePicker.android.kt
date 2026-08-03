package com.jmabilon.chefmate.core.designsystem.provider

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.io.bytestring.ByteString

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteString) -> Unit): ImagePicker {
    val context = LocalContext.current

    // On prépare le launcher Android standard
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        // Read the image bytes from the URI using the content resolver and convert it to ByteString
        val bytes = uri.toByteString(context.contentResolver) ?: return@rememberLauncherForActivityResult

        onImagePicked(bytes)
    }

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                // Launch the image picker with a request for images only
                launcher.launch(
                    input = PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    }
}

private fun Uri.toByteString(contentResolver: ContentResolver): ByteString? {
    return contentResolver.openInputStream(this)?.use { input ->
        ByteString(input.readBytes())
    }
}

actual fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this.toByteArray(), 0, size).asImageBitmap()
}
