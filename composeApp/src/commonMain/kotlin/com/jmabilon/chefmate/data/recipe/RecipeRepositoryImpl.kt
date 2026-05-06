package com.jmabilon.chefmate.data.recipe

import com.jmabilon.chefmate.data.recipe.source.cache.CollectionCacheDataSource
import com.jmabilon.chefmate.data.recipe.source.cache.RecipeCacheDataSource
import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val recipeCacheDataSource: RecipeCacheDataSource,
    private val collectionCacheDataSource: CollectionCacheDataSource
) : RecipeRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    override fun observeRecipeById(recipeId: String): Flow<RecipeDomain> =
        recipeCacheDataSource.observeRecipe(recipeId = recipeId)
            .distinctUntilChanged()
            .transformLatest { recipe ->
                if (recipe == null) {
                    emit(null)
                    getRecipeById(recipeId = recipeId)
                } else {
                    emit(recipe)
                }
            }
            .filterNotNull()

    // =============================================================================================
    // CRUD
    // =============================================================================================

    override suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> {
        return recipeRemoteDataSource.getRecipeById(recipeId = recipeId)
            .onSuccess { recipe ->
                recipeCacheDataSource.cacheRecipe(recipe = recipe)
            }
    }

    override suspend fun createRecipe(
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain> {
        return recipeRemoteDataSource.createRecipe(
            recipe = recipe,
            collectionIds = collectionIds
        )
            .onSuccess { createdRecipe ->
                recipeCacheDataSource.cacheRecipe(recipe = createdRecipe)
            }
    }

    override suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipe(recipeId = recipeId)
            .onSuccess {
                recipeCacheDataSource.invalidate(recipeId = recipeId)
                collectionCacheDataSource.invalidateAll()
            }
    }

    override suspend fun updateRecipe(
        recipeId: String,
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain> {
        return recipeRemoteDataSource.updateRecipe(
            recipeId = recipeId,
            recipe = recipe,
            collectionIds = collectionIds
        )
            .onSuccess { updatedRecipe ->
                recipeCacheDataSource.updateRecipe(recipe = updatedRecipe)
            }
    }


    // =============================================================================================
    // Images
    // =============================================================================================

    override suspend fun getRecipeImageUrl(imagePath: String): Result<String> =
        recipeRemoteDataSource.fetchRecipeImageUrl(imagePath = imagePath)

    override suspend fun uploadRecipeImage(
        recipeId: String,
        imageData: ByteArray,
        extension: String
    ): Result<String> {
        return recipeRemoteDataSource.uploadRecipeImage(
            recipeId = recipeId,
            imageData = imageData,
            extension = extension
        )
    }

    override suspend fun deleteRecipeImage(imagePath: String): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipeImage(imagePath = imagePath)
    }
}
