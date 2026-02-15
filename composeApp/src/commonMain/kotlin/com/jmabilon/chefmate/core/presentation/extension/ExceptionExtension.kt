package com.jmabilon.chefmate.core.presentation.extension

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.error_authentication
import chefmate.composeapp.generated.resources.error_connection
import chefmate.composeapp.generated.resources.error_email_not_confirmed
import chefmate.composeapp.generated.resources.error_rate_limit
import chefmate.composeapp.generated.resources.error_request_failed
import chefmate.composeapp.generated.resources.error_user_already_exists
import chefmate.composeapp.generated.resources.error_weak_password
import com.jmabilon.chefmate.core.network.model.error.NetworkError
import com.jmabilon.chefmate.designsystem.utils.UiText
import com.jmabilon.chefmate.domain.authentication.model.error.AuthenticationError

fun Throwable.toUiText(): UiText {
    return when (this) {
        // Only errors where the user MUST act differently
        is AuthenticationError.InvalidCredentials -> UiText.ResourceString(Res.string.error_authentication)
        is AuthenticationError.EmailNotConfirmed -> UiText.ResourceString(Res.string.error_email_not_confirmed)
        is AuthenticationError.UserAlreadyExists -> UiText.ResourceString(Res.string.error_user_already_exists)
        is AuthenticationError.WeakPassword -> UiText.ResourceString(Res.string.error_weak_password)

        // Only client-side issue
        is NetworkError.NetworkConnectionError,
        is NetworkError.TimeoutError -> UiText.ResourceString(Res.string.error_connection)

        is NetworkError.TooManyRequests,
        is AuthenticationError.RateLimitExceeded -> UiText.ResourceString(Res.string.error_rate_limit)

        // All other issues
        else -> UiText.ResourceString(Res.string.error_request_failed)
    }
}
