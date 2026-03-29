package com.jmabilon.chefmate.domain.collection.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository

interface UpdateRecipeCollectionsUseCase {
    suspend operator fun invoke(recipeId: String, collectionIds: List<String>): Result<Unit>
}

class UpdateRecipeCollectionsUseCaseImpl(
    private val collectionRepository: CollectionRepository,
    private val recipeRepository: RecipeRepository
) : UpdateRecipeCollectionsUseCase {

    override suspend fun invoke(recipeId: String, collectionIds: List<String>): Result<Unit> {
        return collectionRepository.updateRecipeCollections(
            recipeId = recipeId,
            collectionIds = collectionIds
        )
            .onSuccess {
                // After updating the recipe's collections, we need to refresh both the recipe and collections in the cache
                recipeRepository.getRecipeById(recipeId = recipeId)
                collectionRepository.getCollections()
            }
    }
}
