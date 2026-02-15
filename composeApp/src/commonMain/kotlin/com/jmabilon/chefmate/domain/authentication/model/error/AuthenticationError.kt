package com.jmabilon.chefmate.domain.authentication.model.error

sealed class AuthenticationError : Throwable() {
    class InvalidCredentials : AuthenticationError()
    class EmailNotConfirmed : AuthenticationError()
    class UserAlreadyExists : AuthenticationError()
    class WeakPassword : AuthenticationError()
    class RateLimitExceeded : AuthenticationError()
}
