package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain

interface CreateManualRecipeWithImageUseCase {
    suspend operator fun invoke(recipe: RecipeDomain, image: List<Byte>?): Result<Unit>
}

class CreateManualRecipeWithImageUseCaseImpl(
    private val uploadRecipeImageUseCase: UploadRecipeImageUseCase,
    private val createManualRecipeUseCase: CreateManualRecipeUseCase
) : CreateManualRecipeWithImageUseCase {

    override suspend fun invoke(recipe: RecipeDomain, image: List<Byte>?): Result<Unit> {
        val imageUploadResult = if (image != null) {
            uploadRecipeImageUseCase(recipeId = recipe.id, image = image)
        } else {
            Result.success(null)
        }

        return imageUploadResult
            .mapCatching { imageUrl ->
                val recipeWithImage = imageUrl?.let { recipe.copy(imageUrl = it) } ?: recipe

                createManualRecipeUseCase(recipe = recipeWithImage).getOrThrow()
            }
    }
}
