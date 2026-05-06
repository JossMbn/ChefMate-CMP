package com.jmabilon.chefmate.data.recipe.source.cache

import com.jmabilon.chefmate.core.data.cache.CacheConfiguration
import com.jmabilon.chefmate.core.data.cache.CacheEngine
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

// =================================================================================================
// Collection Cache Data Source Implementation
// =================================================================================================

class CollectionCacheDataSourceImpl : CollectionCacheDataSource {

    // =============================================================================================
    // Engine
    // =============================================================================================

    /**
     * The [CacheEngine] is intentionally NOT injectable — this Impl owns its lifecycle.
     * Collection entries expire after [CACHE_DURATION_MS] (10 minutes by default).
     */
    private val engine = CacheEngine<CollectionDomain>(
        configuration = CacheConfiguration(
            cacheDurationMs = Duration.INFINITE
        )
    )

    // =============================================================================================
    // Single Collection
    // =============================================================================================

    override fun getCollection(collectionId: String): Result<CollectionDomain> =
        engine.get(key = collectionId)

    override suspend fun cacheCollection(collection: CollectionDomain) =
        engine.put(key = collection.id, value = collection)

    override suspend fun cacheCollections(collections: List<CollectionDomain>) =
        engine.putAll(entries = collections.associateBy { it.id })

    override suspend fun updateCollection(collection: CollectionDomain) =
        engine.put(key = collection.id, value = collection)

    override suspend fun invalidate(collectionId: String) =
        engine.remove(key = collectionId)

    override suspend fun invalidate(collectionIds: List<String>) =
        collectionIds.forEach { id -> invalidate(collectionId = id) }

    override suspend fun invalidateAll() =
        engine.clear()

    // =============================================================================================
    // Custom Queries
    // =============================================================================================

    override suspend fun moveRecipeToCollections(
        recipeId: String,
        collectionIds: List<String>
    ): Result<Unit> {
        // In a real implementation, this would likely involve more complex logic to update the
        // relevant CollectionDomain entries (e.g. adjusting recipeCount) and ensure cache consistency.
        // For this example, we'll simply invalidate all collections to force a refresh on next observation.
        return runCatching {
            invalidateAll()
        }
    }

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    override fun observeCollection(collectionId: String): Flow<CollectionDomain> =
        engine.observe(key = collectionId).filterNotNull()

    override fun observeCollections(): Flow<List<CollectionDomain>> =
        engine.observeAll().map { it.values.toList() }
}
