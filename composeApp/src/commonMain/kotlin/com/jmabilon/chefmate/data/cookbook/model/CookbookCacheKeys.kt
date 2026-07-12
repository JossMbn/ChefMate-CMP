package com.jmabilon.chefmate.data.cookbook.model

import com.jmabilon.chefmate.core.data.cache.CacheKey

object CookbookCacheKeys {

    private const val FEATURE_KEY = "cookbook"

    data object CookbookList : CacheKey {
        override val name: String = "$FEATURE_KEY/list"
    }
}
