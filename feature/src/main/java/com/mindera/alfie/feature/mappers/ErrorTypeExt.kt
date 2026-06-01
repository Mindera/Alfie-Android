package com.mindera.alfie.feature.mappers

import com.mindera.alfie.core.analytics.events.EventErrorValue
import com.mindera.alfie.feature.model.ApiErrorType
import com.mindera.alfie.repository.result.ErrorType

fun ErrorType.toEventErrorValue(): EventErrorValue = when (this) {
    ErrorType.THROTTLED -> EventErrorValue.THROTTLED
    ErrorType.TIMEOUT -> EventErrorValue.TIMEOUT
    ErrorType.SERVER_ERROR -> EventErrorValue.SERVER_ERROR
    ErrorType.AUTHENTICATION_FAILED -> EventErrorValue.AUTHENTICATION
    ErrorType.NETWORK -> EventErrorValue.NETWORK_ERROR
    else -> EventErrorValue.GENERIC_ERROR
}

fun ErrorType.toApiErrorType(): ApiErrorType = when (this) {
    ErrorType.THROTTLED -> ApiErrorType.Throttled
    ErrorType.SERVER_ERROR -> ApiErrorType.Server
    ErrorType.TIMEOUT, ErrorType.NETWORK -> ApiErrorType.Network
    ErrorType.RESOURCE_NOT_FOUND -> ApiErrorType.NotFound
    else -> ApiErrorType.Generic
}
