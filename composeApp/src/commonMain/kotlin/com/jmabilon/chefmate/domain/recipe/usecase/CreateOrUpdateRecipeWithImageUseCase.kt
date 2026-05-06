package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.feature.recipe.creation.model.recipe.ImageSource

interface CreateOrUpdateRecipeWithImageUseCase {
    suspend operator fun invoke(recipe: RecipeDomain, imageSource: ImageSource?): Result<Unit>
}

class CreateOrUpdateRecipeWithImageUseCaseImpl(
    private val uploadRecipeImageUseCase: UploadRecipeImageUseCase,
    private val createManualRecipeUseCase: CreateManualRecipeUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase
) : CreateOrUpdateRecipeWithImageUseCase {

    override suspend fun invoke(recipe: RecipeDomain, imageSource: ImageSource?): Result<Unit> {
        val imageUploadResult = when (imageSource) {
            is ImageSource.ByteArray -> {
                uploadRecipeImageUseCase(recipeId = recipe.id, image = imageSource.bytes)
            }

            is ImageSource.Url -> {
                Result.success(imageSource.url)
            }

            null -> {
                Result.success(null)
            }
        }

        return imageUploadResult
            .mapCatching { imageUrl ->
                val recipeWithImage = recipe.copy(imageUrl = imageUrl)

                if (recipe.id.isEmpty()) {
                    createManualRecipeUseCase(recipe = recipeWithImage).getOrThrow()
                } else {
                    updateRecipeUseCase(recipe = recipeWithImage).getOrThrow()
                }
            }
    }
}
