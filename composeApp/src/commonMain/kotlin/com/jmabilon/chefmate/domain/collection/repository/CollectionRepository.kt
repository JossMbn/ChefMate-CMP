package com.jmabilon.chefmate.domain.collection.repository

import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    /**
     * Returns a [Flow] that emits the full list of all cached collections.
     * Falls back to a remote fetch of page 0 on cache miss.
     */
    fun observeCollections(): Flow<List<CollectionDomain>>

    // =============================================================================================
    // CRUD
    // =============================================================================================

    suspend fun getCollections(): Result<List<CollectionDomain>>

    suspend fun createCollection(collectionName: String): Result<CollectionDomain>

    suspend fun deleteCollection(collectionId: String): Result<Unit>

    suspend fun renameCollection(collectionId: String, newName: String): Result<Unit>

    /**
     * Moves a recipe to the given set of collections (replaces previous membership).
     * Invalidates both recipe and collection caches for cross-screen reactivity.
     */
    suspend fun updateRecipeCollections(recipeId: String, collectionIds: List<String>): Result<Unit>

    suspend fun toggleRecipeToFavoriteCollection(recipeId: String): Result<Unit>
}
