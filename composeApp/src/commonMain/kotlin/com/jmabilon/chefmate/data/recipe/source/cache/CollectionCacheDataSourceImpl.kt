package com.jmabilon.chefmate.data.recipe.source.cache

import com.jmabilon.chefmate.core.data.cache.CacheConfiguration
import com.jmabilon.chefmate.core.data.cache.CacheEngine
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
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

    override suspend fun renameCollection(collectionId: String, collectionName: String) {
        val collection = getCollection(collectionId = collectionId).getOrNull() ?: run {
            invalidateAll()
            return
        }
        val renamedCollection = collection.copy(name = collectionName)

        cacheCollection(collection = renamedCollection)
    }

    override suspend fun removeRecipesInCollections(recipes: List<String>) {
        val allCollections = observeCollections().first()

        val updatedCollections = allCollections.map { collection ->
            val updatedRecipeIds = collection.recipes.filterNot { it.id in recipes }
            collection.copy(recipes = updatedRecipeIds)
        }

        cacheCollections(collections = updatedCollections)
    }

    override suspend fun invalidate(collectionId: String) =
        engine.remove(key = collectionId)

    override suspend fun invalidate(collectionIds: List<String>) =
        collectionIds.forEach { id -> invalidate(collectionId = id) }

    override suspend fun invalidateAll() =
        engine.clear()

    // =============================================================================================
    // Custom Queries
    // =============================================================================================

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    override fun observeCollection(collectionId: String): Flow<CollectionDomain> =
        engine.observe(key = collectionId).filterNotNull()

    override fun observeCollections(): Flow<List<CollectionDomain>> =
        engine.observeAll().map { it.values.toList() }
}
