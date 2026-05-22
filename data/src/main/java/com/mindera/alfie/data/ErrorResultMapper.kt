package com.mindera.alfie.data

import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.network.exception.GraphNetworkException.BadRequestException
import com.mindera.alfie.network.exception.GraphNetworkException.ClientException
import com.mindera.alfie.network.exception.GraphNetworkException.ConflictException
import com.mindera.alfie.network.exception.GraphNetworkException.InvalidResponseException
import com.mindera.alfie.network.exception.GraphNetworkException.MethodNotAllowedException
import com.mindera.alfie.network.exception.GraphNetworkException.NetworkException
import com.mindera.alfie.network.exception.GraphNetworkException.NotFoundException
import com.mindera.alfie.network.exception.GraphNetworkException.ServerException
import com.mindera.alfie.network.exception.GraphNetworkException.ThrottledException
import com.mindera.alfie.network.exception.GraphNetworkException.TimeoutException
import com.mindera.alfie.network.exception.GraphNetworkException.UnProcessableEntityException
import com.mindera.alfie.network.exception.GraphNetworkException.UnauthorizedException
import com.mindera.alfie.network.exception.GraphNetworkException.UnexpectedException
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType.AUTHENTICATION_FAILED
import com.mindera.alfie.repository.result.ErrorType.BAD_REQUEST
import com.mindera.alfie.repository.result.ErrorType.CONFLICT
import com.mindera.alfie.repository.result.ErrorType.GENERIC_ERROR
import com.mindera.alfie.repository.result.ErrorType.INVALID_REQUEST
import com.mindera.alfie.repository.result.ErrorType.METHOD_NOT_ALLOWED
import com.mindera.alfie.repository.result.ErrorType.NETWORK
import com.mindera.alfie.repository.result.ErrorType.RESOURCE_NOT_FOUND
import com.mindera.alfie.repository.result.ErrorType.UNKNOWN
import com.mindera.alfie.repository.result.ErrorType.UN_PROCESSABLE_ENTITY

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
        is ThrottledException -> GENERIC_ERROR
        is TimeoutException -> UNKNOWN
        is UnexpectedException -> UNKNOWN
    }

    return ErrorResult(
        type = errorType,
        errorMessage = message,
        code = code.toString()
    )
}
