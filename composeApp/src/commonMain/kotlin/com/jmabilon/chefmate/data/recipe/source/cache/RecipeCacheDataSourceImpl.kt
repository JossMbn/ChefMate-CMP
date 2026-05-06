package com.jmabilon.chefmate.data.recipe.source.cache

import com.jmabilon.chefmate.core.data.cache.CacheConfiguration
import com.jmabilon.chefmate.core.data.cache.CacheEngine
import com.jmabilon.chefmate.domain.recipe.model.RecipeCollectionInfoDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

// =================================================================================================
// Recipe Cache Data Source Implementation
// =================================================================================================

class RecipeCacheDataSourceImpl : RecipeCacheDataSource {

    // =============================================================================================
    // Engine
    // =============================================================================================

    /**
     * The [CacheEngine] is intentionally NOT injectable — this Impl owns its lifecycle.
     */
    private val engine = CacheEngine<RecipeDomain>(
        configuration = CacheConfiguration(
            cacheDurationMs = Duration.INFINITE
        )
    )

    // =============================================================================================
    // Single Recipe
    // =============================================================================================

    override fun getRecipe(recipeId: String): Result<RecipeDomain> =
        engine.get(key = recipeId)

    override suspend fun cacheRecipe(recipe: RecipeDomain) =
        engine.put(key = recipe.id, value = recipe)

    override suspend fun updateRecipe(recipe: RecipeDomain) =
        engine.put(key = recipe.id, value = recipe)

    override suspend fun invalidate(recipeId: String) =
        engine.remove(key = recipeId)

    override suspend fun invalidateAll() =
        engine.clear()

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    override fun observeRecipe(recipeId: String): Flow<RecipeDomain?> =
        engine.observe(key = recipeId)

    /**
     * Derived from [observeRecipe] — when the recipe's [RecipeDomain.collections] list
     * changes (e.g. after [updateRecipe]), this flow re-emits automatically.
     */
    override fun observeCollectionsForRecipe(recipeId: String): Flow<List<RecipeCollectionInfoDomain>> =
        engine.observe(key = recipeId).map { recipe ->
            recipe?.collections ?: emptyList()
        }
}
