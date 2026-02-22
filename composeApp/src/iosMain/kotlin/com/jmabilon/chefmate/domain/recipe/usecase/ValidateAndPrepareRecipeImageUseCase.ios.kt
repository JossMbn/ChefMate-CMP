package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.error.RecipeImageError
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

actual fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase {
    return ValidateAndPrepareRecipeImageUseCaseImpl()
}

@OptIn(ExperimentalForeignApi::class)
class ValidateAndPrepareRecipeImageUseCaseImpl : BaseValidateAndPrepareRecipeImageUseCase(),
    ValidateAndPrepareRecipeImageUseCase {

    override suspend fun processImage(imageData: ByteArray): PreparedImage {
        val nsData = imageData.toNSData()
        val originalImage = UIImage.imageWithData(nsData)
            ?: throw RecipeImageError.InvalidImageData()

        val resizedImage = resizeImage(originalImage)
        val compressedData = compressToTarget(resizedImage)

        return PreparedImage(
            data = compressedData,
            extension = "jpg" // TODO : find library to convert into webp.
        )
    }

    private fun resizeImage(image: UIImage): UIImage {
        val width = image.size.useContents { width.toInt() }
        val height = image.size.useContents { height.toInt() }

        val (newWidth, newHeight) = calculateNewDimensions(width, height, MAX_DIMENSION)

        if (newWidth == width && newHeight == height) {
            return image
        }

        val newSize = CGSizeMake(newWidth.toDouble(), newHeight.toDouble())
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.drawInRect(CGRectMake(0.0, 0.0, newWidth.toDouble(), newHeight.toDouble()))
        val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        return resizedImage ?: image
    }

    private fun compressToTarget(image: UIImage): ByteArray {
        var quality = INITIAL_QUALITY.toDouble() / 100.0
        var compressedData: NSData?

        do {
            compressedData = UIImageJPEGRepresentation(image, quality)
            quality -= QUALITY_STEP.toDouble() / 100.0
        } while (compressedData != null &&
            shouldReduceQuality(compressedData.length.toInt(), (quality * 100).toInt())
        )

        if (compressedData == null || compressedData.length.toInt() > MAX_COMPRESSED_SIZE_BYTES) {
            throw RecipeImageError.CompressionFailed()
        }

        return compressedData.toByteArray()
    }

    // Helper extensions
    private fun ByteArray.toNSData(): NSData {
        return this.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), this.size.toULong())
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        return ByteArray(this.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
            }
        }
    }
}
