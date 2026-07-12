package com.jmabilon.chefmate.domain.cookbook.repository

import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import kotlinx.coroutines.flow.Flow

interface CookbookRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    /**
     * Returns a [Flow] that emits the full list of all cached cookbooks.
     * Falls back to a remote fetch of page 0 on cache miss.
     */
    fun observeCookbooks(): Flow<List<CookbookDomain>>

    // =============================================================================================
    // CRUD
    // =============================================================================================

    suspend fun createCookbook(cookbookName: String): Result<CookbookDomain>

    suspend fun deleteCookbook(cookbookId: String): Result<Unit>

    suspend fun renameCookbook(cookbookId: String, newName: String): Result<Unit>

    /**
     * Moves a recipe to the given set of cookbooks (replaces previous membership).
     * Invalidates both recipe and cookbook caches for cross-screen reactivity.
     */
    suspend fun updateRecipeCookbooks(recipeId: String, cookbookIds: List<String>): Result<Unit>

    suspend fun toggleRecipeToFavoriteCookbook(recipeId: String): Result<Unit>
}
