package au.com.alfie.ecomm.data

import au.com.alfie.ecomm.network.exception.GraphNetworkException
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
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType.AUTHENTICATION_FAILED
import au.com.alfie.ecomm.repository.result.ErrorType.BAD_REQUEST
import au.com.alfie.ecomm.repository.result.ErrorType.CONFLICT
import au.com.alfie.ecomm.repository.result.ErrorType.GENERIC_ERROR
import au.com.alfie.ecomm.repository.result.ErrorType.INVALID_REQUEST
import au.com.alfie.ecomm.repository.result.ErrorType.METHOD_NOT_ALLOWED
import au.com.alfie.ecomm.repository.result.ErrorType.NETWORK
import au.com.alfie.ecomm.repository.result.ErrorType.RESOURCE_NOT_FOUND
import au.com.alfie.ecomm.repository.result.ErrorType.UNKNOWN
import au.com.alfie.ecomm.repository.result.ErrorType.UN_PROCESSABLE_ENTITY

fun GraphNetworkException.toErrorResult(): ErrorResult {
    val errorType = when (this) {
        is NetworkException -> NETWORK
        is BadRequestException -> BAD_REQUEST
        is UnauthorizedException -> AUTHENTICATION_FAILED
        is NotFoundException -> RESOURCE_NOT_FOUND
        is MethodNotAllowedException -> METHOD_NOT_ALLOWED
        is ConflictException -> CONFLICT
        is UnProcessableEntityException -> UN_PROCESSABLE_ENTITY
        is InvalidResponseException -> INVALID_REQUEST
        is ClientException -> GENERIC_ERROR
        is ServerException -> GENERIC_ERROR
        is UnexpectedException -> UNKNOWN
    }

    return ErrorResult(
        type = errorType,
        errorMessage = message,
        code = code.toString()
    )
}
