package com.jmabilon.chefmate.core.designsystem.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.skia.Image
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun rememberImagePicker(onImagePicked: (List<Byte>?) -> Unit): ImagePicker {
    // On récupère le ViewController racine pour afficher la modale iOS
    val window = UIApplication.sharedApplication.keyWindow
    val rootViewController = window?.rootViewController

    // IMPORTANT : Revenir sur le thread UI (Main) pour le callback Compose
    fun returnImagePickerByteList(bytes: List<Byte>?) {
        dispatch_async(dispatch_get_main_queue()) {
            onImagePicked(bytes?.toList())
        }
    }

    val delegate = remember {
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(true, null)

                val result = didFinishPicking.firstOrNull() as? PHPickerResult
                val provider = result?.itemProvider
                    ?: return returnImagePickerByteList(null)

                val imageType = "public.image"

                // Vérifie si le provider contient une image
                if (!provider.hasItemConformingToTypeIdentifier(imageType)) {
                    print("Picked item is not an image")
                    return returnImagePickerByteList(null)
                }

                // Charge les bytes (NSData) de l'image
                provider.loadDataRepresentationForTypeIdentifier(
                    typeIdentifier = imageType
                ) { nsData, error ->
                    if (nsData == null) {
                        print("nsData is null: $error")
                        returnImagePickerByteList(null)
                        return@loadDataRepresentationForTypeIdentifier
                    }

                    // Convert NSData → ByteArray
                    val length = nsData.length.toInt()
                    val byteArray = ByteArray(length)
                    memcpy(byteArray.refTo(0), nsData.bytes, nsData.length)

                    returnImagePickerByteList(byteArray.toList())
                }
            }
        }
    }

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                val configuration = PHPickerConfiguration().apply {
                    selectionLimit = 1
                    filter = PHPickerFilter.imagesFilter
                }

                val picker = PHPickerViewController(configuration)
                picker.delegate = delegate
                rootViewController?.presentViewController(
                    viewControllerToPresent = picker,
                    animated = true,
                    completion = null
                )
            }
        }
    }
}

actual fun ImmutableList<Byte>.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this.toByteArray()).toComposeImageBitmap()
}
