package com.jmabilon.chefmate.core.supabase.extension

import com.jmabilon.chefmate.core.network.model.error.NetworkError
import com.jmabilon.chefmate.domain.authentication.model.error.AuthenticationError
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
        Result.failure(e.toError())
    } catch (_: HttpRequestTimeoutException) {
        Result.failure(NetworkError.TimeoutError())
    } catch (_: HttpRequestException) {
        Result.failure(NetworkError.NetworkConnectionError())
    } catch (_: SerializationException) {
        Result.failure(NetworkError.SerializationError())
    } catch (e: Exception) {
        Result.failure(NetworkError.Unknown(e.message))
    }

/**
 * Extension function to convert a [RestException] into a more specific error type based on the error code and HTTP status code.
 *
 * @return A [Throwable] representing the specific error that occurred during the REST API call.
 */
private fun RestException.toError(): Throwable = when (error) {
    "invalid_credentials" -> AuthenticationError.InvalidCredentials()
    "email_not_confirmed" -> AuthenticationError.EmailNotConfirmed()
    "user_already_exists" -> AuthenticationError.UserAlreadyExists()
    "weak_password" -> AuthenticationError.WeakPassword()
    "over_email_send_rate_limit" -> AuthenticationError.RateLimitExceeded()

    else -> when (statusCode) {
        400 -> NetworkError.BadRequest(message)
        401 -> NetworkError.Unauthorized()
        403 -> NetworkError.Forbidden()
        404 -> NetworkError.NotFound()
        422 -> NetworkError.ValidationError(message)
        429 -> NetworkError.TooManyRequests()
        in 500..599 -> NetworkError.ServerError()
        else -> NetworkError.Unknown(message)
    }
}
