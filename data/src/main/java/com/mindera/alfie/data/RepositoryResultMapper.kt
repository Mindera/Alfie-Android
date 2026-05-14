package com.mindera.alfie.data

import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import timber.log.Timber

fun <T> Result<T>.toRepositoryResult(): RepositoryResult<T> = map {
    RepositoryResult.Success(it)
}.getOrElse {
    Timber.e(it)
    RepositoryResult.Error(it.toErrorResult())
}

private fun Throwable.toErrorResult() = when (this) {
    is GraphNetworkException -> toErrorResult()
    else -> ErrorResult(type = ErrorType.UNKNOWN)
}
