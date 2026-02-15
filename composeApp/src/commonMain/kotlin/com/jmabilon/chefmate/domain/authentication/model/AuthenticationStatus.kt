package com.jmabilon.chefmate.domain.authentication.model

enum class AuthenticationStatus {
    Authenticated,
    NotAuthenticated,
    RefreshFailure,
    Initializing
}
