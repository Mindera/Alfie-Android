package com.mindera.alfie.feature.model

sealed interface ApiErrorType {
    data object Throttled : ApiErrorType
    data object Server : ApiErrorType
    data object Network : ApiErrorType
    data object NotFound : ApiErrorType
    data object Generic : ApiErrorType
}
