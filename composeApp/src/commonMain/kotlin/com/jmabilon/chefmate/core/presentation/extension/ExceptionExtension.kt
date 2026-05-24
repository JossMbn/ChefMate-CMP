package com.jmabilon.chefmate.core.presentation.extension

import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.error_authentication
import chefmate.composeapp.generated.resources.error_connection
import chefmate.composeapp.generated.resources.error_email_not_confirmed
import chefmate.composeapp.generated.resources.error_rate_limit
import chefmate.composeapp.generated.resources.error_request_failed
import chefmate.composeapp.generated.resources.error_user_already_exists
import chefmate.composeapp.generated.resources.error_weak_password
import com.jmabilon.chefmate.core.domain.DataError
import com.jmabilon.chefmate.core.presentation.UiText

fun Throwable.toUiText(): UiText {
    return when (this) {
        // Only errors where the user MUST act differently
        is DataError.Authentication.InvalidCredentials -> UiText.ResourceString(Res.string.error_authentication)
        is DataError.Authentication.EmailNotConfirmed -> UiText.ResourceString(Res.string.error_email_not_confirmed)
        is DataError.Authentication.UserAlreadyExists -> UiText.ResourceString(Res.string.error_user_already_exists)
        is DataError.Authentication.WeakPassword -> UiText.ResourceString(Res.string.error_weak_password)

        // Only client-side issue
        is DataError.Network.NoInternet,
        is DataError.Network.RequestTimeout -> UiText.ResourceString(Res.string.error_connection)

        is DataError.Network.TooManyRequests,
        is DataError.Authentication.RateLimitExceeded -> UiText.ResourceString(Res.string.error_rate_limit)

        // All other issues
        else -> UiText.ResourceString(Res.string.error_request_failed)
    }
}
