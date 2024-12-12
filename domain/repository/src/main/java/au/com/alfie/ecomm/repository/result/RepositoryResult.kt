package au.com.alfie.ecomm.repository.result

sealed class RepositoryResult<out T> {

    data class Success<out T>(val data: T) : RepositoryResult<T>()

    data class Error(val errorResult: ErrorResult) : RepositoryResult<Nothing>()
}

suspend fun <A, B> RepositoryResult<A>.flatMap(
    flatMap: suspend (A) -> RepositoryResult<B>
): RepositoryResult<B> {
    return when (this) {
        is RepositoryResult.Success -> flatMap(data)
        is RepositoryResult.Error -> this
    }
}

suspend fun <A, B> RepositoryResult<A>.map(
    map: suspend (A) -> B
): RepositoryResult<B> {
    return when (this) {
        is RepositoryResult.Success -> RepositoryResult.Success(map(data))
        is RepositoryResult.Error -> this
    }
}

inline fun <T> RepositoryResult<T>.onSuccess(
    onSuccess: (T) -> Unit
): RepositoryResult<T> {
    if (this is RepositoryResult.Success) {
        onSuccess(data)
    }
    return this
}

inline fun <R, T> RepositoryResult<T>.fold(
    onSuccess: (value: T) -> R,
    onError: (error: ErrorResult) -> R
): R = when (this) {
    is RepositoryResult.Success -> onSuccess(data)
    is RepositoryResult.Error -> onError(errorResult)
}
