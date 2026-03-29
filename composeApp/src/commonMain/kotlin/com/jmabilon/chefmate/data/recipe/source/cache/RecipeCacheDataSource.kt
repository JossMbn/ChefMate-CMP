package com.jmabilon.chefmate.data.recipe.source.cache

import com.jmabilon.chefmate.core.data.cache.CacheError
import com.jmabilon.chefmate.domain.recipe.model.RecipeCollectionInfoDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.coroutines.flow.Flow

// =================================================================================================
// Recipe Cache Data Source
// =================================================================================================

/**
 * In-memory cache for [RecipeDomain] objects.
 *
 * Provides reactive [Flow]-based observation so that any update to a cached recipe
 * is automatically propagated to all active observers — including cross-screen flows
 * such as "collections for a recipe" and "recipes for a collection".
 *
 * On [CacheError], callers are expected to fall back to the remote data source and
 * replenish the cache via [cacheRecipe].
 */
interface RecipeCacheDataSource {

    // =============================================================================================
    // Single Recipe
    // =============================================================================================

    /**
     * Returns the cached [RecipeDomain] for [recipeId].
     * Fails with [CacheError.NotFound] or [CacheError.Expired] when the entry is absent or stale.
     */
    fun getRecipe(recipeId: String): Result<RecipeDomain>

    /**
     * Stores [recipe] in the cache (insert or replace).
     */
    suspend fun cacheRecipe(recipe: RecipeDomain)

    /**
     * Updates an existing cache entry for [recipe]. Semantically identical to [cacheRecipe]
     * but signals intent of mutation rather than initial population.
     */
    suspend fun updateRecipe(recipe: RecipeDomain)

    /**
     * Removes the cache entry for [recipeId].
     */
    suspend fun invalidate(recipeId: String)

    /**
     * Removes all recipe entries from the cache.
     */
    suspend fun invalidateAll()

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    /**
     * Emits the cached [RecipeDomain] for [recipeId], or `null` when not cached.
     * Re-emits whenever the entry changes.
     */
    fun observeRecipe(recipeId: String): Flow<RecipeDomain?>

    /**
     * Emits the list of [RecipeCollectionInfoDomain] that belong to [recipeId].
     * Derived from [observeRecipe] — automatically updates when the recipe's
     * collection membership changes.
     */
    fun observeCollectionsForRecipe(recipeId: String): Flow<List<RecipeCollectionInfoDomain>>
}
