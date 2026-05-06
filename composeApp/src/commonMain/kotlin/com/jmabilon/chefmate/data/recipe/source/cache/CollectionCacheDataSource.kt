package com.jmabilon.chefmate.data.recipe.source.cache

import com.jmabilon.chefmate.core.data.cache.CacheError
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow

// =================================================================================================
// Collection Cache Data Source
// =================================================================================================

/**
 * In-memory cache for [CollectionDomain] objects.
 *
 * Exposes reactive [Flow]-based observation over the cached collection list.
 * When a recipe's collection membership changes, the repository is responsible for
 * calling [updateCollection] so that the affected [CollectionDomain] entries
 * (e.g. updated [CollectionDomain.recipeCount]) are reflected here.
 *
 * On [CacheError], callers are expected to fall back to the remote data source.
 */
interface CollectionCacheDataSource {

    // =============================================================================================
    // Single Collection
    // =============================================================================================

    /**
     * Returns the cached [CollectionDomain] for [collectionId].
     * Fails with [CacheError.NotFound] or [CacheError.Expired] when absent or stale.
     */
    fun getCollection(collectionId: String): Result<CollectionDomain>

    /**
     * Stores [collection] in the cache (insert or replace).
     */
    suspend fun cacheCollection(collection: CollectionDomain)

    /**
     * Atomically stores a list of collections in the cache, all stamped with the same timestamp.
     */
    suspend fun cacheCollections(collections: List<CollectionDomain>)

    /**
     * Updates an existing cache entry for [collection]. Semantically identical to [cacheCollection]
     * but signals intent of mutation rather than initial population.
     */
    suspend fun updateCollection(collection: CollectionDomain)

    /**
     * Removes the cache entry for [collectionId].
     */
    suspend fun invalidate(collectionId: String)

    /**
     * Removes the cache entries for [collectionIds].
     */
    suspend fun invalidate(collectionIds: List<String>)

    /**
     * Removes all collection entries from the cache.
     */
    suspend fun invalidateAll()

    // =============================================================================================
    // Custom Queries
    // =============================================================================================

    suspend fun moveRecipeToCollections(recipeId: String, collectionIds: List<String>): Result<Unit>

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    /**
     * Emits the cached [CollectionDomain] for [collectionId], or `null` when not cached.
     * Re-emits whenever the entry changes.
     */
    fun observeCollection(collectionId: String): Flow<CollectionDomain>

    /**
     * Emits the full list of all non-expired cached collections.
     * Re-emits whenever any collection is added, updated, or removed.
     */
    fun observeCollections(): Flow<List<CollectionDomain>>
}
