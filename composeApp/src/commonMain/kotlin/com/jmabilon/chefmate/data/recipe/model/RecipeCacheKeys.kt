package com.jmabilon.chefmate.data.recipe.model

import com.jmabilon.chefmate.core.data.cache.CacheKey

object RecipeCacheKeys {

    private const val FEATURE_KEY = "recipe"

    data class Recipe(val recipeId: String) : CacheKey {
        override val name: String = "$FEATURE_KEY/$recipeId"
    }
}
