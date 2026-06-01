package com.mindera.alfie.network.exception

import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_BAD_REQUEST
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_CONFLICT
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_NOT_FOUND
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS_EXTENDED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UNAUTHORIZED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT
import kotlin.time.Duration

sealed class GraphNetworkException(override val message: String) : Exception(message) {

    open val code: Int = 0

    data class BadRequestException(
        override val code: Int = HTTP_CLIENT_ERROR_BAD_REQUEST.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class UnauthorizedException(
        override val code: Int = HTTP_CLIENT_ERROR_UNAUTHORIZED.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class NotFoundException(
        override val code: Int = HTTP_CLIENT_ERROR_NOT_FOUND.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class MethodNotAllowedException(
        override val code: Int = HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class ConflictException(
        override val code: Int = HTTP_CLIENT_ERROR_CONFLICT.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class UnProcessableEntityException(
        override val code: Int = HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT.code,
        override val message: String
    ) : GraphNetworkException(message)

    data class ThrottledException(
        override val code: Int,
        override val message: String,
        val retryAfter: Duration? = null
    ) : GraphNetworkException(message) {
        init {
            require(code == HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS.code || code == HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS_EXTENDED.code)
        }
    }

    data class ClientException(
        override val code: Int,
        override val message: String
    ) : GraphNetworkException(message)

    data class ServerException(
        override val code: Int,
        override val message: String
    ) : GraphNetworkException(message)

    data class NetworkException(
        override val code: Int,
        override val message: String
    ) : GraphNetworkException(message)

    class TimeoutException(
        override val message: String,
        cause: Throwable? = null
    ) : GraphNetworkException(message) {
        init {
            cause?.let(::initCause)
        }
    }

    data class InvalidResponseException(
        override val message: String
    ) : GraphNetworkException(message)

    data class UnexpectedException(
        override val message: String
    ) : GraphNetworkException(message)
}
