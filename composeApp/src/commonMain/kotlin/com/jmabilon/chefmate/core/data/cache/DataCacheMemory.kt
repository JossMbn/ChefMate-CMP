package com.jmabilon.chefmate.core.data.cache

import com.jmabilon.chefmate.core.common.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * This cache implementation will only survive as long as the Application process
 *
 * No persistence
 */
class DataCacheMemory(private val timeProvider: TimeProvider = TimeProviderImpl()) : DataCache {

    private val cache = mutableMapOf<String, CacheEntry>()
    private val cacheUpdates = MutableSharedFlow<String>(extraBufferCapacity = 3) // Emits when cache changes
    private val mutex = Mutex() // Mutex to protect concurrent access to the cache

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(key: CacheKey): T? {
        val entry = mutex.withLock { cache[key.name] }
        return if (entry == null) {
            key.log("no cache entry")
            null
        } else {
            val timeToLive = entry.expiry - timeProvider.now()
            if (timeToLive.isNegative()) {
                key.log("entry expired ${timeToLive.inWholeSeconds} seconds ago")
                null
            } else {
                key.log("cache hit: time to live ${timeToLive.inWholeSeconds} seconds")
                entry.data as T
            }
        }
    }

    override suspend fun <T : Any> set(key: CacheKey, value: T, timeout: CachePolicy.Timeout) {
        val expiryDuration = when (timeout) {
            is CachePolicy.Timeout.MaxAge -> timeout.duration
            is CachePolicy.Timeout.PointInTime -> {
                if (timeout.time < timeProvider.now()) {
                    key.log("Friendly warning: This cache entry has expired before it had a chance to live!")
                }
                timeout.time - timeProvider.now()
            }

            CachePolicy.Timeout.Always -> Duration.ZERO
            CachePolicy.Timeout.Never -> Duration.INFINITE
        }
        val entry = CacheEntry(value, timeProvider.now() + expiryDuration)
        key.log("Setting cache Entry: $entry")
        mutex.withLock {
            cache[key.name] = entry
        }

        // Notify listeners
        val result = cacheUpdates.tryEmit(key.name)
        if (!result) {
            key.log("⛔⛔Failed to emit cache update")
        }
    }

    override suspend fun clear(key: CacheKey, shouldNotify: Boolean) {
        mutex.withLock {
            cache.remove(key.name)
        }
        if (shouldNotify) cacheUpdates.tryEmit(key.name) // Notify flow subscribers if required
    }

    override suspend fun clear(shouldNotify: Boolean) {
        mutex.withLock {
            cache.clear()
        }
        if (shouldNotify) cacheUpdates.tryEmit("ALL") // Notify all subscribers if required
    }

    override fun observe(key: CacheKey): Flow<Unit> {
        return cacheUpdates
            .filter { it == key.name || it == "ALL" }
            .map { /* no-op */ }
    }

    private fun CacheKey.log(message: String) {
        Logger.d(tag = "DataCacheMemory", message = "${this.name}: $message")
    }
}
