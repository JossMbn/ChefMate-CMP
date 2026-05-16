package com.jmabilon.chefmate.data.collection

import com.jmabilon.chefmate.data.collection.source.remote.CollectionRemoteDataSource
import com.jmabilon.chefmate.data.recipe.source.cache.CollectionCacheDataSource
import com.jmabilon.chefmate.data.recipe.source.cache.RecipeCacheDataSource
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionRepositoryImpl(
    private val collectionRemoteDataSource: CollectionRemoteDataSource,
    private val collectionCacheDataSource: CollectionCacheDataSource,
    private val recipeCacheDataSource: RecipeCacheDataSource
) : CollectionRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    override fun observeCollections(): Flow<List<CollectionDomain>> =
        collectionCacheDataSource.observeCollections()
            .distinctUntilChanged()
            .transformLatest { collections ->
                if (collections.isEmpty()) {
                    emit(emptyList())
                    getCollections()
                } else {
                    emit(collections)
                }
            }

    // =============================================================================================
    // CRUD
    // =============================================================================================

    override suspend fun getCollections(): Result<List<CollectionDomain>> {
        return collectionRemoteDataSource.getCollections()
            .onSuccess { fetchedCollections ->
                collectionCacheDataSource.cacheCollections(collections = fetchedCollections)
            }
    }

    override suspend fun createCollection(collectionName: String): Result<CollectionDomain> {
        return collectionRemoteDataSource.createCollection(collectionName = collectionName)
            .onSuccess { newCollection ->
                collectionCacheDataSource.cacheCollection(collection = newCollection)
            }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return collectionRemoteDataSource.deleteCollection(collectionId = collectionId)
            .onSuccess {
                collectionCacheDataSource.invalidate(collectionId = collectionId)
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
                collectionCacheDataSource.renameCollection(
                    collectionId = collectionId,
                    collectionName = newName
                )
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
    }

    override suspend fun toggleRecipeToFavoriteCollection(recipeId: String): Result<Unit> {
        return collectionRemoteDataSource.toggleRecipeToFavoriteCollection(recipeId = recipeId)
            .onSuccess {
                recipeCacheDataSource.invalidate(recipeId = recipeId)
                collectionCacheDataSource.invalidateAll()
            }
            .mapCatching { /* no-op */ }
    }
}
