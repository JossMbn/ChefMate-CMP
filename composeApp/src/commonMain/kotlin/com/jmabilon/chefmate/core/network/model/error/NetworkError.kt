package com.jmabilon.chefmate.core.network.model.error

sealed class NetworkError : Throwable() {
    class NetworkConnectionError : NetworkError()
    class TimeoutError : NetworkError()
    class SerializationError : NetworkError()
    class Unauthorized : NetworkError()
    class Forbidden : NetworkError()
    class NotFound : NetworkError()
    class TooManyRequests : NetworkError()
    class ServerError : NetworkError()
    data class BadRequest(override val message: String?) : NetworkError()
    data class ValidationError(override val message: String?) : NetworkError()
    data class Unknown(override val message: String? = null) : NetworkError()
}
