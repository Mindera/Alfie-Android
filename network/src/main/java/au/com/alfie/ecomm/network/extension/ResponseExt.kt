package au.com.alfie.ecomm.network.extension

import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_BAD_REQUEST
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_CONFLICT
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_NOT_FOUND
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UNAUTHORIZED
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.INTERNAL_HTTP_ERROR
import au.com.alfie.ecomm.network.exception.GraphNetworkException.BadRequestException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ClientException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ConflictException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.InvalidResponseException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.MethodNotAllowedException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NotFoundException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ServerException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnProcessableEntityException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnauthorizedException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnexpectedException
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
