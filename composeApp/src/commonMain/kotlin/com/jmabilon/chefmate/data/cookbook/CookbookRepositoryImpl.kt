package com.jmabilon.chefmate.data.cookbook

import com.jmabilon.chefmate.core.data.cache.CachePolicy
import com.jmabilon.chefmate.core.data.cache.DataCache
import com.jmabilon.chefmate.core.data.cache.createCachedFlow
import com.jmabilon.chefmate.core.domain.extension.asEmptyResult
import com.jmabilon.chefmate.data.cookbook.model.CookbookCacheKeys
import com.jmabilon.chefmate.data.cookbook.remote.CookbookRemoteDataSource
import com.jmabilon.chefmate.data.recipe.model.RecipeCacheKeys
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class CookbookRepositoryImpl(
    private val cookbookRemoteDataSource: CookbookRemoteDataSource,
    private val cache: DataCache
) : CookbookRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    override fun observeCookbooks(): Flow<List<CookbookDomain>> = cache.createCachedFlow(
        key = CookbookCacheKeys.CookbookList,
        policy = CachePolicy(
            time = CachePolicy.Timeout.Never
        ),
        block = { cookbookRemoteDataSource.getCookbooks() }
    )

    // =============================================================================================
    // CRUD
    // =============================================================================================

    override suspend fun createCookbook(cookbookName: String): Result<CookbookDomain> {
        return cookbookRemoteDataSource.createCookbook(cookbookName = cookbookName)
            .onSuccess {
                cache.clear()
            }
    }

    override suspend fun deleteCookbook(cookbookId: String): Result<Unit> {
        return cookbookRemoteDataSource.deleteCookbook(cookbookId = cookbookId)
            .onSuccess {
                cache.clear()
            }
    }

    override suspend fun renameCookbook(
        cookbookId: String,
        newName: String
    ): Result<Unit> {
        return cookbookRemoteDataSource.renameCookbook(
            cookbookId = cookbookId,
            newName = newName
        )
            .onSuccess {
                cache.clear(key = CookbookCacheKeys.CookbookList)
            }
    }

    override suspend fun updateRecipeCookbooks(
        recipeId: String,
        cookbookIds: List<String>
    ): Result<Unit> {
        return cookbookRemoteDataSource.moveRecipeToCookbooks(
            recipeId = recipeId,
            cookbookIds = cookbookIds
        )
            .onSuccess {
                cache.clear(key = CookbookCacheKeys.CookbookList)
                cache.clear(key = RecipeCacheKeys.Recipe(recipeId))
            }
    }

    override suspend fun toggleRecipeToFavoriteCookbook(recipeId: String): Result<Unit> {
        return cookbookRemoteDataSource.toggleRecipeToFavoriteCookbook(recipeId = recipeId)
            .onSuccess {
                cache.clear(key = CookbookCacheKeys.CookbookList)
                cache.clear(key = RecipeCacheKeys.Recipe(recipeId))
            }
            .asEmptyResult()
    }
}
