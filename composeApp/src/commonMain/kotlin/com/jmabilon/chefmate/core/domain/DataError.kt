package com.jmabilon.chefmate.core.domain

sealed class DataError : Throwable() {

    sealed class Network : DataError() {
        class BadRequest : Network()
        class RequestTimeout : Network()
        class Unauthorized : Network()
        class Forbidden : Network()
        class NotFound : Network()
        class Conflict : Network()
        class TooManyRequests : Network()
        class NoInternet : Network()
        class PayloadTooLarge : Network()
        class ServerError : Network()
        class ServiceUnavailable : Network()
        class Serialization : Network()
        data class Unknown(val errorMessage: String?) : Network()
    }

    sealed class Authentication : DataError() {
        class InvalidCredentials : Authentication()
        class EmailNotConfirmed : Authentication()
        class UserAlreadyExists : Authentication()
        class WeakPassword : Authentication()
        class RateLimitExceeded : Authentication()
    }
}
