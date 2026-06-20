package com.jmabilon.chefmate.feature.recipe.creation.domain.usecase

import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UploadRecipeImageUseCase {
    suspend operator fun invoke(recipeId: String, image: List<Byte>): Result<String>
}

class UploadRecipeImageUseCaseImpl(
    private val validateAndPrepareRecipeImageUseCase: ValidateAndPrepareRecipeImageUseCase,
    private val recipeRepository: RecipeRepository
) : UploadRecipeImageUseCase {

    override suspend fun invoke(recipeId: String, image: List<Byte>): Result<String> {
        val verificationResult = withContext(Dispatchers.Default) {
            val imageData = image.toByteArray()

            return@withContext validateAndPrepareRecipeImageUseCase(imageData = imageData).getOrNull()
        }

        if (verificationResult == null) {
            return Result.failure(IllegalStateException("Image validation failed"))
        }

        return recipeRepository.uploadRecipeImage(
            recipeId = recipeId,
            imageData = verificationResult.data,
            extension = verificationResult.extension
        )
            .mapCatching { imagePath ->
                recipeRepository.getRecipeImageUrl(imagePath = imagePath).getOrThrow()
            }
    }
}
