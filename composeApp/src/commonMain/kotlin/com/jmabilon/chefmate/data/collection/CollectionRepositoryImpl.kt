package com.jmabilon.chefmate.data.collection

import com.jmabilon.chefmate.core.data.cache.CachePolicy
import com.jmabilon.chefmate.core.data.cache.DataCache
import com.jmabilon.chefmate.core.data.cache.createCachedFlow
import com.jmabilon.chefmate.core.domain.extension.asEmptyResult
import com.jmabilon.chefmate.data.collection.remote.CollectionRemoteDataSource
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionRepositoryImpl(
    private val collectionRemoteDataSource: CollectionRemoteDataSource,
    private val cache: DataCache
) : CollectionRepository {

    companion object {
        private const val CACHE_KEY_PREFIX = "collections"
        private const val COLLECTIONS_CACHE_KEY = "$CACHE_KEY_PREFIX/list"

        private fun createCollectionCacheKey(collectionId: String): String = "$CACHE_KEY_PREFIX/$collectionId"
    }

    // =============================================================================================
    // Observation
    // =============================================================================================

    override fun observeCollections(): Flow<List<CollectionDomain>> = cache.createCachedFlow(
        key = COLLECTIONS_CACHE_KEY,
        policy = CachePolicy(
            time = CachePolicy.Timeout.Never
        ),
        block = { collectionRemoteDataSource.getCollections() }
    )

    // =============================================================================================
    // CRUD
    // =============================================================================================

    override suspend fun createCollection(collectionName: String): Result<CollectionDomain> {
        return collectionRemoteDataSource.createCollection(collectionName = collectionName)
            .onSuccess { newCollection ->
                cache.clear(shouldNotify = true)
                cache.set(
                    key = createCollectionCacheKey(newCollection.id),
                    value = newCollection,
                    timeout = CachePolicy.Timeout.Never
                )
            }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return collectionRemoteDataSource.deleteCollection(collectionId = collectionId)
            .onSuccess {
                cache.clear(shouldNotify = true)
            }
    }

    override suspend fun renameCollection(
        collectionId: String,
        newName: String
    ): Result<Unit> {
        return collectionRemoteDataSource.renameCollection(
            collectionId = collectionId,
            newName = newName
        )
            .onSuccess {
                cache.clear(shouldNotify = true)
            }
    }

    override suspend fun updateRecipeCollections(
        recipeId: String,
        collectionIds: List<String>
    ): Result<Unit> {
        return collectionRemoteDataSource.moveRecipeToCollections(
            recipeId = recipeId,
            collectionIds = collectionIds
        )
            .onSuccess {
                cache.clear(shouldNotify = true)
            }
    }

    override suspend fun toggleRecipeToFavoriteCollection(recipeId: String): Result<Unit> {
        return collectionRemoteDataSource.toggleRecipeToFavoriteCollection(recipeId = recipeId)
            .onSuccess {
                cache.clear(shouldNotify = true)
            }
            .asEmptyResult()
    }
}
