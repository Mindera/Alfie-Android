package au.com.alfie.ecomm.data

import au.com.alfie.ecomm.network.exception.GraphNetworkException
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType
import au.com.alfie.ecomm.repository.result.RepositoryResult
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
