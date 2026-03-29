package com.jmabilon.chefmate.core.data.cache

// =================================================================================================
// Cache Error
// =================================================================================================

/**
 * Sealed class representing errors that can occur when accessing the cache.
 * Extends [Throwable] so it can be used with [Result.failure].
 */
sealed class CacheError : Throwable() {

    /**
     * The cache is globally disabled via [CacheConfiguration.isEnabled] — no entries are stored or returned.
     */
    class Disabled : CacheError()

    /**
     * The requested key was not found in the cache.
     */
    class NotFound : CacheError()

    /**
     * The cache entry exists but has expired based on [CacheConfiguration.cacheDurationMs].
     */
    class Expired : CacheError()
}

fun Throwable.isCacheError(): Boolean = this is CacheError
