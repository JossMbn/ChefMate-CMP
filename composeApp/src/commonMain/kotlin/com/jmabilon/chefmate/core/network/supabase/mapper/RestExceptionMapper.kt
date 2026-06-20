package com.jmabilon.chefmate.core.network.supabase.mapper

import com.jmabilon.chefmate.core.domain.DataError
import io.github.jan.supabase.exceptions.RestException

/**
 * Extension function to convert a [RestException] into a more specific error type based on the error code and HTTP status code.
 *
 * @return A [Throwable] representing the specific error that occurred during the REST API call.
 */
fun RestException.toError(): Throwable = when (error) {
    "invalid_credentials" -> DataError.Authentication.InvalidCredentials()
    "email_not_confirmed" -> DataError.Authentication.EmailNotConfirmed()
    "user_already_exists" -> DataError.Authentication.UserAlreadyExists()
    "weak_password" -> DataError.Authentication.WeakPassword()
    "over_email_send_rate_limit" -> DataError.Authentication.RateLimitExceeded()

    else -> when (statusCode) {
        400 -> DataError.Network.BadRequest()
        401 -> DataError.Network.Unauthorized()
        403 -> DataError.Network.Forbidden()
        404 -> DataError.Network.NotFound()
        409 -> DataError.Network.Conflict()
        413 -> DataError.Network.PayloadTooLarge()
        429 -> DataError.Network.TooManyRequests()
        503 -> DataError.Network.ServiceUnavailable()
        in 500..599 -> DataError.Network.ServerError()
        else -> DataError.Network.Unknown(message)
    }
}
