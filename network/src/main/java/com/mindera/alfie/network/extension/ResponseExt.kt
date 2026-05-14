package com.mindera.alfie.network.extension

import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_BAD_REQUEST
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_CONFLICT
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_NOT_FOUND
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UNAUTHORIZED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT
import com.mindera.alfie.network.exception.ExceptionErrorCodes.INTERNAL_HTTP_ERROR
import com.mindera.alfie.network.exception.GraphNetworkException.BadRequestException
import com.mindera.alfie.network.exception.GraphNetworkException.ClientException
import com.mindera.alfie.network.exception.GraphNetworkException.ConflictException
import com.mindera.alfie.network.exception.GraphNetworkException.InvalidResponseException
import com.mindera.alfie.network.exception.GraphNetworkException.MethodNotAllowedException
import com.mindera.alfie.network.exception.GraphNetworkException.NetworkException
import com.mindera.alfie.network.exception.GraphNetworkException.NotFoundException
import com.mindera.alfie.network.exception.GraphNetworkException.ServerException
import com.mindera.alfie.network.exception.GraphNetworkException.UnProcessableEntityException
import com.mindera.alfie.network.exception.GraphNetworkException.UnauthorizedException
import com.mindera.alfie.network.exception.GraphNetworkException.UnexpectedException
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloHttpException

private const val HTTP_ERROR_NUMBER_CUTOFF = 3
private const val HTTP_CLIENT_ERROR_MIN_CODE = 400
private const val HTTP_CLIENT_ERROR_MAX_CODE = 499
private const val HTTP_SERVER_ERROR_MIN_CODE = 500
private const val HTTP_SERVER_ERROR_MAX_CODE = 599

fun <T : Operation.Data> ApolloResponse<T>.toException(): Throwable = when {
    hasErrors().not() && exception == null && data == null -> {
        InvalidResponseException(message = "Call successful, but response body is null")
    }
    exception is ApolloHttpException -> {
        mapException(
            code = (exception as ApolloHttpException).statusCode,
            message = (exception as ApolloHttpException).message.orEmpty()
        )
    }
    else -> {
        val errorMessage = errors?.first()?.message.orEmpty()
        val extractedCode = errorMessage.filter(Char::isDigit)
        val errorCode = if (extractedCode.isNotEmpty()) {
            extractedCode.take(HTTP_ERROR_NUMBER_CUTOFF).toInt()
        } else {
            INTERNAL_HTTP_ERROR.code
        }
        mapException(
            code = errorCode,
            message = errorMessage
        )
    }
}

private fun mapException(code: Int, message: String): Throwable =
    when (code) {
        INTERNAL_HTTP_ERROR.code -> NetworkException(
            code = code,
            message = message
        )
        HTTP_CLIENT_ERROR_BAD_REQUEST.code -> BadRequestException(message = message)
        HTTP_CLIENT_ERROR_UNAUTHORIZED.code -> UnauthorizedException(message = message)
        HTTP_CLIENT_ERROR_NOT_FOUND.code -> NotFoundException(message = message)
        HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED.code -> MethodNotAllowedException(message = message)
        HTTP_CLIENT_ERROR_CONFLICT.code -> ConflictException(message = message)
        HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT.code -> UnProcessableEntityException(message = message)
        in HTTP_CLIENT_ERROR_MIN_CODE..HTTP_CLIENT_ERROR_MAX_CODE -> ClientException(
            code = code,
            message = message
        )
        in HTTP_SERVER_ERROR_MIN_CODE..HTTP_SERVER_ERROR_MAX_CODE -> ServerException(
            code = code,
            message = message
        )
        else -> UnexpectedException(message = message)
    }
