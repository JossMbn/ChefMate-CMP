package com.jmabilon.chefmate.feature.entrypoint.domain.model

enum class AuthenticationStatus {
    Authenticated,
    NotAuthenticated,
    RefreshFailure,
    Initializing
}
