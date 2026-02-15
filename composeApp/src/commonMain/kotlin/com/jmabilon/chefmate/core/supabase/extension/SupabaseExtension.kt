package com.jmabilon.chefmate.core.supabase.extension

import com.jmabilon.chefmate.core.network.model.error.NetworkError
import com.jmabilon.chefmate.domain.authentication.model.error.AuthenticationError
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

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
