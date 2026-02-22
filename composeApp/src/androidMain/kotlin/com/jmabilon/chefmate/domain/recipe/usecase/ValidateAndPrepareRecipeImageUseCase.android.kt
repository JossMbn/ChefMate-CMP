package com.jmabilon.chefmate.domain.recipe.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import com.jmabilon.chefmate.domain.recipe.model.error.RecipeImageError
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

actual fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase {
    return ValidateAndPrepareRecipeImageUseCaseImpl()
}

class ValidateAndPrepareRecipeImageUseCaseImpl : BaseValidateAndPrepareRecipeImageUseCase(),
    ValidateAndPrepareRecipeImageUseCase {

    override suspend fun processImage(imageData: ByteArray): PreparedImage {
        val originalBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            ?: throw RecipeImageError.InvalidImageData()

        val rotatedBitmap = fixOrientation(imageData, originalBitmap)
        val resizedBitmap = resizeBitmap(rotatedBitmap)

        val (format, extension) = getCompressionFormat()
        val compressedData = compressToTarget(resizedBitmap, format)

        return PreparedImage(
            data = compressedData,
            extension = extension
        )
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val (newWidth, newHeight) = calculateNewDimensions(
            bitmap.width,
            bitmap.height,
            MAX_DIMENSION
        )

        if (newWidth == bitmap.width && newHeight == bitmap.height) {
            return bitmap
        }

        return bitmap.scale(newWidth, newHeight)
    }

    private fun compressToTarget(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat
    ): ByteArray {
        val outputStream = ByteArrayOutputStream()
        var quality = INITIAL_QUALITY

        do {
            outputStream.reset()
            bitmap.compress(format, quality, outputStream)
            quality -= QUALITY_STEP
        } while (shouldReduceQuality(outputStream.size(), quality))

        if (outputStream.size() > MAX_COMPRESSED_SIZE_BYTES) {
            throw RecipeImageError.InvalidImageData()
        }

        return outputStream.toByteArray()
    }

    private fun getCompressionFormat(): Pair<Bitmap.CompressFormat, String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY to "webp"
        } else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP to "webp"
        }
    }

    private fun fixOrientation(imageData: ByteArray, bitmap: Bitmap): Bitmap {
        val inputStream = ByteArrayInputStream(imageData)
        val exif = try {
            ExifInterface(inputStream)
        } catch (_: Exception) {
            return bitmap
        }

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height,
            matrix, true
        )
    }
}
