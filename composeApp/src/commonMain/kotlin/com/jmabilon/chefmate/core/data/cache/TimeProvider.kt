package com.jmabilon.chefmate.core.data.cache

import kotlin.time.Clock
import kotlin.time.Instant

interface TimeProvider {
    fun now(): Instant
}

internal class TimeProviderImpl : TimeProvider {
    override fun now(): Instant = Clock.System.now()
}
