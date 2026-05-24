package com.jmabilon.chefmate.core.data.cache

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

data class CachePolicy(
    val time: Timeout = Timeout.Always
) {
    companion object {
        private val DEFAULT_CACHE_DURATION = 10.minutes
    }

    sealed interface Timeout {
        object Never : Timeout
        object Always : Timeout
        data class MaxAge(val duration: Duration = DEFAULT_CACHE_DURATION) : Timeout
        data class PointInTime(val time: Instant) : Timeout
    }
}
