package com.jmabilon.chefmate.core.data.cache

import kotlin.time.Clock
import kotlin.time.Duration

// =================================================================================================
// Cache Configuration
// =================================================================================================

/**
 * Configuration for [CacheEngine].
 *
 * @param cacheDurationMs The maximum age of a cache entry in milliseconds before it is considered expired.
 * @param isDebug When `true`, the cache logs additional debug information.
 * @param isEnabled When `false`, [CacheEngine.get] always returns [CacheError.NotFound],
 *   effectively forcing every read to fall through to the remote source.
 */
data class CacheConfiguration(
    val cacheDurationMs: Duration,
    val isDebug: Boolean = false,
    val isEnabled: Boolean = true
) {

    /**
     * Returns `true` if the entry stored at [timestampMs] has lived longer than [cacheDurationMs].
     */
    fun isExpired(timestampMs: Long): Boolean {
        if (cacheDurationMs == Duration.INFINITE) return false

        val nowMs = Clock.System.now().toEpochMilliseconds()
        return (nowMs - timestampMs) > cacheDurationMs.inWholeMilliseconds
    }
}
