package com.jmabilon.chefmate.core.supabase.extension

import io.github.jan.supabase.SupabaseClient

suspend fun <T> SupabaseClient.safeExecution(block: suspend SupabaseClient.() -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
