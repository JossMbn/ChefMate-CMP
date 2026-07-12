package com.jmabilon.chefmate.data.recipe

import com.jmabilon.chefmate.core.data.cache.CachePolicy
import com.jmabilon.chefmate.core.data.cache.DataCache
import com.jmabilon.chefmate.core.data.cache.createCachedFlow
import com.jmabilon.chefmate.data.cookbook.model.CookbookCacheKeys
import com.jmabilon.chefmate.data.recipe.model.RecipeCacheKeys
import com.jmabilon.chefmate.data.recipe.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
    private val cache: DataCache
) : RecipeRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    override fun observeRecipeById(recipeId: String): Flow<RecipeDomain> = cache.createCachedFlow(
        key = RecipeCacheKeys.Recipe(recipeId),
        policy = CachePolicy(time = CachePolicy.Timeout.Never)
    ) { recipeRemoteDataSource.getRecipeById(recipeId = recipeId) }

    // =============================================================================================
    // CRUD
    // =============================================================================================

    override suspend fun createRecipe(
        recipe: RecipeDomain,
        cookbookIds: List<String>
    ): Result<RecipeDomain> {
        return recipeRemoteDataSource.createRecipe(
            recipe = recipe,
            cookbookIds = cookbookIds
        )
            .onSuccess { recipe ->
                cache.clear(key = CookbookCacheKeys.CookbookList)
                cache.set(
                    key = RecipeCacheKeys.Recipe(recipe.id),
                    value = recipe
                )
            }
    }

    override suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipe(recipeId = recipeId)
            .onSuccess {
                cache.clear(key = CookbookCacheKeys.CookbookList)
            }
    }

    override suspend fun updateRecipe(recipe: RecipeDomain): Result<RecipeDomain> {
        return recipeRemoteDataSource.updateRecipe(
            recipe = recipe
        )
            .onSuccess {
                cache.clear(key = CookbookCacheKeys.CookbookList)
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

    override suspend fun scanRecipeFromImage(imageData: List<Byte>): Result<RecipeDomain> {
        return recipeRemoteDataSource.scanRecipeFromImage(imageData = imageData)
    }
}
