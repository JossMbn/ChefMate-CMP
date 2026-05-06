package com.jmabilon.chefmate.data.recipe.source.cache.model

import com.jmabilon.chefmate.domain.collection.model.CollectionDetailsDomain
import com.jmabilon.chefmate.domain.collection.model.CollectionRecipeInfoDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType

// =================================================================================================
// Cached Collection Details
// =================================================================================================

/**
 * Internal cache representation of [CollectionDetailsDomain] with pagination metadata.
 *
 * Recipes are stored per-page in [recipesByPage], keyed by a page number (0-based).
 * This allows incremental accumulation: each `loadMore()` call adds a new page entry
 * without overwriting previously cached pages. Observers always receive the full
 * accumulated recipe list across all loaded pages.
 */
data class CachedCollectionDetails(
    val id: String,
    val title: String,
    val recipeCount: Int,
    val systemType: CollectionSystemType?,
    /**
     * Map from page number → recipes fetched for that page.
     * Insert-order is preserved so [allRecipes] respects page ordering.
     */
    val recipesByPage: Map<Int, List<CollectionRecipeInfoDomain>> = emptyMap()
) {

    // =============================================================================================
    // Derived
    // =============================================================================================

    /**
     * All recipes accumulated across every loaded page, in page-number order.
     */
    val allRecipes: List<CollectionRecipeInfoDomain>
        get() = recipesByPage.entries
            .sortedBy { it.key }
            .flatMap { it.value }

    // =============================================================================================
    // Pagination helpers
    // =============================================================================================

    /**
     * Returns `true` if [page] has already been cached.
     */
    fun hasPage(page: Int): Boolean = recipesByPage.containsKey(page)

    /**
     * Returns a copy with [recipes] merged for [page].
     * Metadata (title, recipeCount, systemType) is refreshed from [source].
     */
    fun withPage(
        page: Int,
        recipes: List<CollectionRecipeInfoDomain>,
        source: CollectionDetailsDomain
    ): CachedCollectionDetails = copy(
        title = source.title,
        recipeCount = source.recipeCount,
        systemType = source.systemType,
        recipesByPage = recipesByPage + (page to recipes)
    )

    // =============================================================================================
    // Conversion
    // =============================================================================================

    /**
     * Converts to [CollectionDetailsDomain] using the full accumulated recipe list.
     */
    fun toCollectionDetailsDomain(): CollectionDetailsDomain = CollectionDetailsDomain(
        id = id,
        title = title,
        recipeCount = recipeCount,
        systemType = systemType,
        recipes = allRecipes
    )

    companion object {

        /**
         * Creates a fresh [CachedCollectionDetails] from the first fetched page.
         */
        fun fromDomain(details: CollectionDetailsDomain, page: Int): CachedCollectionDetails =
            CachedCollectionDetails(
                id = details.id,
                title = details.title,
                recipeCount = details.recipeCount,
                systemType = details.systemType,
                recipesByPage = mapOf(page to details.recipes)
            )
    }
}
