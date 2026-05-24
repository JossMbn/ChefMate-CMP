package com.jmabilon.chefmate.core.data.cache

import kotlin.time.Instant

data class CacheEntry(val data: Any, val expiry: Instant)
