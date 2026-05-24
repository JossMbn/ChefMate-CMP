package com.jmabilon.chefmate.feature.recipe.creation.domain.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import com.jmabilon.chefmate.feature.recipe.creation.presentation.model.recipe.ImageSource

interface CreateOrUpdateRecipeWithImageUseCase {
    suspend operator fun invoke(recipe: RecipeDomain, imageSource: ImageSource?): Result<Unit>
}

class CreateOrUpdateRecipeWithImageUseCaseImpl(
    private val uploadRecipeImageUseCase: UploadRecipeImageUseCase,
    private val recipeRepository: RecipeRepository
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
                    recipeRepository.createRecipe(
                        recipe = recipeWithImage,
                        collectionIds = emptyList()
                    ).getOrThrow()
                } else {
                    recipeRepository.updateRecipe(recipe = recipeWithImage).getOrThrow()
                }
            }
    }
}
