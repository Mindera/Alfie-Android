package au.com.alfie.ecomm.domain

import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UseCaseInteractor {

    suspend fun <R> UseCaseInteractor.run(response: RepositoryResult<R>): UseCaseResult<R> =
        response.doOnResult(
            onSuccess = { UseCaseResult.Success(it) },
            onError = { UseCaseResult.Error(it) }
        )

    suspend fun <R, T> UseCaseInteractor.runMap(
        response: RepositoryResult<T>,
        mapUseCase: suspend (T) -> (R)
    ): UseCaseResult<R> =
        response.doOnResult(
            onSuccess = {
                withContext(Dispatchers.Default) {
                    UseCaseResult.Success(mapUseCase(it))
                }
            },
            onError = { UseCaseResult.Error(it) }
        )

    suspend fun <R, T> UseCaseInteractor.runMapResult(
        response: RepositoryResult<T>,
        mapUseCase: suspend (T) -> (UseCaseResult<R>)
    ): UseCaseResult<R> =
        response.doOnResult(
            onSuccess = {
                withContext(Dispatchers.Default) {
                    mapUseCase(it)
                }
            },
            onError = { UseCaseResult.Error(it) }
        )
}
