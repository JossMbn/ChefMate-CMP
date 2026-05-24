package com.jmabilon.chefmate.core.data.cache

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

interface DataCache {

    suspend fun <T : Any> get(key: String): T?

    suspend fun <T : Any> set(key: String, value: T, timeout: CachePolicy.Timeout)

    suspend fun clear(key: String, shouldNotify: Boolean = false) // Default: No notification

    suspend fun clear(shouldNotify: Boolean = false) // Default: No notification

    fun observe(key: String): Flow<Unit>
}

inline fun <reified T : Any> DataCache.createCachedFlow(
    key: String,
    policy: CachePolicy,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> Result<T>,
): Flow<T> = flow {
    emitCachedValueIfExists(key) { keyParam ->
        get(keyParam)
    }

    observe(key).collect {
        val cachedValue = get<T>(key)

        if (cachedValue != null) {
            emit(cachedValue)
        } else {
            val value = block().getOrThrow()
            set(key, value, policy.time)
        }
    }
}.onStart {
    if (get<T>(key) == null) {
        val value = block().getOrThrow()

        set(key, value, policy.time)
    }
}.flowOn(dispatcher)

suspend inline fun <reified T : Any> FlowCollector<T>.emitCachedValueIfExists(
    key: String,
    noinline getFunction: suspend (String) -> T?
) {
    val cachedResult = getFunction(key)
    if (cachedResult != null) {
        emit(cachedResult)
    }
}
