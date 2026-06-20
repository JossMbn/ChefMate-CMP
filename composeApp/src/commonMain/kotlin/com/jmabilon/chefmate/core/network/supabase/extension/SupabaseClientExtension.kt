package com.jmabilon.chefmate.core.network.supabase.extension

import com.jmabilon.chefmate.core.domain.DataError
import com.jmabilon.chefmate.core.network.supabase.mapper.toError
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes a block of code that interacts with the Supabase client and wraps the result in a [Result] type.
 * Catches various exceptions that may occur during the execution and maps them to appropriate error types.
 *
 * @param block The block of code to execute, which has access to the [SupabaseClient] instance.
 * @return A [Result] containing either the successful result of the block or an error if an exception was thrown.
 */
suspend fun <T> SupabaseClient.safeExecution(block: suspend SupabaseClient.() -> T): Result<T> =
    try {
        val result = block()
        Result.success(result)
    } catch (e: CancellationException) {
        throw e
    } catch (e: RestException) {
        e.printStackTrace()
        Result.failure(e.toError())
    } catch (e: HttpRequestTimeoutException) {
        e.printStackTrace()
        Result.failure(DataError.Network.RequestTimeout())
    } catch (e: HttpRequestException) {
        e.printStackTrace()
        Result.failure(DataError.Network.NoInternet())
    } catch (e: SerializationException) {
        e.printStackTrace()
        Result.failure(DataError.Network.Serialization())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(DataError.Network.Unknown(e.message))
    }
