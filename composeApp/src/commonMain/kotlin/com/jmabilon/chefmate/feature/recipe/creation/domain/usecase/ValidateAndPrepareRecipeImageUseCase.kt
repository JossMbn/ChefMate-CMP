package com.jmabilon.chefmate.feature.recipe.creation.domain.usecase

import com.jmabilon.chefmate.domain.recipe.model.error.RecipeImageError

data class PreparedImage(
    val data: ByteArray,
    val extension: String
)

interface ValidateAndPrepareRecipeImageUseCase {

    suspend operator fun invoke(imageData: ByteArray): Result<PreparedImage>
}

expect fun createValidateAndPrepareRecipeImageUseCase(): ValidateAndPrepareRecipeImageUseCase

abstract class BaseValidateAndPrepareRecipeImageUseCase : ValidateAndPrepareRecipeImageUseCase {

    companion object {
        const val MAX_ORIGINAL_SIZE_BYTES = 10 * 1024 * 1024
        const val MAX_COMPRESSED_SIZE_BYTES = 500 * 1024
        const val MAX_DIMENSION = 1200
        const val INITIAL_QUALITY = 85
        const val MIN_QUALITY = 50
        const val QUALITY_STEP = 5
    }

    override suspend operator fun invoke(imageData: ByteArray): Result<PreparedImage> =
        runCatching {
            validateImageSize(imageData)
            processImage(imageData)
        }

    private fun validateImageSize(imageData: ByteArray) {
        if (imageData.size > MAX_ORIGINAL_SIZE_BYTES) {
            throw RecipeImageError.ImageSizeTooLarge()
        }
    }

    protected fun calculateNewDimensions(
        currentWidth: Int,
        currentHeight: Int,
        maxDimension: Int
    ): Pair<Int, Int> {
        if (currentWidth <= maxDimension && currentHeight <= maxDimension) {
            return currentWidth to currentHeight
        }

        val ratio = currentWidth.toFloat() / currentHeight.toFloat()
        return if (currentWidth > currentHeight) {
            maxDimension to (maxDimension / ratio).toInt()
        } else {
            (maxDimension * ratio).toInt() to maxDimension
        }
    }

    protected fun shouldReduceQuality(currentSize: Int, quality: Int): Boolean {
        return currentSize > MAX_COMPRESSED_SIZE_BYTES && quality >= MIN_QUALITY
    }

    protected abstract suspend fun processImage(imageData: ByteArray): PreparedImage
}
