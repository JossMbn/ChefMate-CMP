package com.jmabilon.chefmate.core.data.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Clock

// =================================================================================================
// Cache Entry
// =================================================================================================

/**
 * Wraps a cached [value] together with the [timestampMs] at which it was stored.
 */
data class CacheEntry<T>(
    val value: T,
    val timestampMs: Long
)

// =================================================================================================
// Cache Engine
// =================================================================================================

/**
 * A generic, thread-safe, reactive in-memory cache backed by a [MutableStateFlow].
 *
 * This class is **not injectable** — each [com.jmabilon.chefmate.data] cache data source
 * owns its own instance. Thread-safety is enforced by a [Mutex] on all write operations
 * while reads are lock-free (snapshot from [MutableStateFlow.value]).
 *
 * @param T The type of values stored in this cache.
 * @param configuration Controls duration, debug mode, and whether the cache is active.
 */
class CacheEngine<T>(
    private val configuration: CacheConfiguration
) {

    // =============================================================================================
    // Internal State
    // =============================================================================================

    private val _cache = MutableStateFlow<Map<String, CacheEntry<T>>>(emptyMap())

    // =============================================================================================
    // Read — lock-free snapshot
    // =============================================================================================

    /**
     * Returns [Result.success] with the cached value if present and not expired.
     * Returns [Result.failure] with:
     * - [CacheError.NotFound] when the cache is disabled or the key is absent.
     * - [CacheError.Expired] when the entry has exceeded [CacheConfiguration.cacheDurationMs].
     */
    fun get(key: String): Result<T> {
        if (!configuration.isEnabled) return Result.failure(CacheError.Disabled())
        val entry = _cache.value[key] ?: return Result.failure(CacheError.NotFound())
        if (configuration.isExpired(entry.timestampMs)) return Result.failure(CacheError.Expired())
        return Result.success(entry.value)
    }

    fun getAll(): Result<List<T>> {
        if (!configuration.isEnabled) return Result.failure(CacheError.Disabled())
        val entries = _cache.value.values
            .filterNot { configuration.isExpired(it.timestampMs) }
            .map { it.value }
        return Result.success(entries)
    }

    // =============================================================================================
    // Reactive Observation
    // =============================================================================================

    /**
     * Returns a [Flow] that emits the entire set of non-expired cached values as a [Map] whenever
     * any entry changes. Expired entries are filtered out on each emission.
     */
    fun observeAll(): Flow<Map<String, T>> = _cache.map { entries ->
        if (!configuration.isEnabled) return@map emptyMap()
        entries
            .filterValues { !configuration.isExpired(it.timestampMs) }
            .mapValues { it.value.value }
    }

    /**
     * Returns a [Flow] that emits the cached value for [key], or `null` if absent or expired.
     */
    fun observe(key: String): Flow<T?> = observeAll().map { it[key] }

    // =============================================================================================
    // Write
    // =============================================================================================

    /**
     * Inserts or replaces the entry for [key] with [value], stamped with the current time.
     */
    fun put(key: String, value: T) {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        _cache.update { current ->
            current + (key to CacheEntry(value = value, timestampMs = nowMs))
        }
    }

    /**
     * Atomically inserts or replaces multiple entries, all stamped with the same timestamp.
     */
    fun putAll(entries: Map<String, T>) {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        _cache.update { current ->
            current + entries.mapValues { CacheEntry(value = it.value, timestampMs = nowMs) }
        }
    }

    /**
     * Removes the entry for [key]. No-op if the key does not exist.
     */
    fun remove(key: String) {
        _cache.update { it - key }
    }

    /**
     * Removes all entries from the cache.
     */
    fun clear() {
        _cache.update { emptyMap() }
    }
}
