package au.com.alfie.ecomm.domain

import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

sealed class UseCaseResult<out D> {
    /**
     * The use case was ran successfully
     * @param data The data output
     */
    data class Success<D>(val data: D) : UseCaseResult<D>()

    /**
     * There was an error while performing the use case logic
     * @param error The error output
     */
    data class Error(val error: ErrorResult) : UseCaseResult<Nothing>()
}

/**
 * Inline extension method to call over an RepositoryResult (typically the result of an useCase), passing two blocks:
 * an implementation to the success (That receive an parameter of type D and returns an UseCaseResult of types F to success)
 */
inline fun <D, F> RepositoryResult<D>.doOnResult(
    onSuccess: (D) -> UseCaseResult<F>,
    onError: (ErrorResult) -> UseCaseResult<F>
): UseCaseResult<F> {
    return when (this) {
        is RepositoryResult.Success -> onSuccess(data)
        is RepositoryResult.Error -> onError(errorResult)
    }
}

/**
 * Inline extension method to call over an UseCaseResult, passing two blocks:
 * an implementation to the success (That receive an parameter of type D and does not return anything)
 * and an implementation to the error(That receive an parameter of type ErrorResult and does not return anything)
 */
inline fun <D> UseCaseResult<D>.doOnResult(
    onSuccess: (D) -> Unit,
    onError: (ErrorResult) -> Unit
) {
    when (this) {
        is UseCaseResult.Success -> onSuccess(data)
        is UseCaseResult.Error -> onError(error)
    }
}

inline fun <D> UseCaseResult<D>.onSuccess(
    onSuccess: (D) -> Unit
): UseCaseResult<D> {
    if (this is UseCaseResult.Success) {
        onSuccess(data)
    }
    return this
}

suspend fun <A, B> UseCaseResult<A>.map(
    map: suspend (A) -> B
): UseCaseResult<B> {
    return when (this) {
        is UseCaseResult.Success -> UseCaseResult.Success(map(data))
        is UseCaseResult.Error -> this
    }
}

suspend fun <A, B> UseCaseResult<A>.flatMap(
    flatMap: suspend (A) -> UseCaseResult<B>
): UseCaseResult<B> {
    return when (this) {
        is UseCaseResult.Success -> flatMap(data)
        is UseCaseResult.Error -> this
    }
}

suspend fun <A, B, C> UseCaseResult<A>.combine(
    secondResult: suspend (A) -> UseCaseResult<B>,
    combine: suspend (A, B) -> C
): UseCaseResult<C> {
    return when (val firstResult = this) {
        is UseCaseResult.Success -> {
            secondResult(firstResult.data).map { secondData ->
                combine(firstResult.data, secondData)
            }
        }

        is UseCaseResult.Error -> firstResult
    }
}

suspend fun <A, B, C> UseCaseResult<A>.combineIf(
    condition: Boolean,
    secondResult: suspend (A) -> UseCaseResult<B>,
    combine: suspend (A, B) -> C,
    notCombine: suspend (A) -> C
): UseCaseResult<C> {
    return when (val firstResult = this) {
        is UseCaseResult.Success -> {
            if (condition) {
                secondResult(firstResult.data).map { secondData ->
                    combine(firstResult.data, secondData)
                }
            } else {
                UseCaseResult.Success(notCombine(firstResult.data))
            }
        }
        is UseCaseResult.Error -> firstResult
    }
}

suspend fun <A, B, C> UseCaseResult<A>.combine(
    secondResult: UseCaseResult<B>,
    combine: suspend (A, B) -> C
): UseCaseResult<C> {
    return when (val firstResult = this) {
        is UseCaseResult.Success -> {
            when (secondResult) {
                is UseCaseResult.Success -> {
                    firstResult.map { firstData ->
                        combine(firstData, secondResult.data)
                    }
                }

                is UseCaseResult.Error -> secondResult
            }
        }

        is UseCaseResult.Error -> firstResult
    }
}

suspend fun <A, B, C> combine(
    first: suspend () -> UseCaseResult<A>,
    second: suspend () -> UseCaseResult<B>,
    combine: suspend (A, B) -> C
): UseCaseResult<C> = coroutineScope {
    val firstRequest = async { first() }
    val secondRequest = async { second() }

    val firstResult = firstRequest.await()
    val secondResult = secondRequest.await()

    firstResult.combine(secondResult, combine)
}

fun <D> UseCaseResult<D>.asResult(): Result<D> = when (this) {
    is UseCaseResult.Error -> Result.failure(error)
    is UseCaseResult.Success -> Result.success(data)
}
