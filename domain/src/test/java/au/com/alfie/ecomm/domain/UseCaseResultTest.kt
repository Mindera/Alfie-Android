package au.com.alfie.ecomm.domain

import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType
import au.com.alfie.ecomm.repository.result.RepositoryResult
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class UseCaseResultTest {

    @Test
    fun `RepositoryResult doOnResult - on success`() {
        val repositoryResult = RepositoryResult.Success("data")

        val result = repositoryResult.doOnResult(
            onSuccess = { data -> UseCaseResult.Success(data) },
            onError = { error -> UseCaseResult.Error(error) }
        )

        assertEquals("data", (result as UseCaseResult.Success).data)
    }

    @Test
    fun `RepositoryResult doOnResult - on error`() {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val repositoryResult = RepositoryResult.Error(errorResult)

        val result = repositoryResult.doOnResult(
            onSuccess = { data -> UseCaseResult.Success(data) },
            onError = { error -> UseCaseResult.Error(error) }
        )

        assertEquals(errorResult, (result as UseCaseResult.Error).error)
    }

    @Test
    fun `UseCaseResult doOnResult - on success`() {
        val useCaseResult = UseCaseResult.Success("data")
        val onSuccess = spyk<(String) -> Unit>()

        useCaseResult.doOnResult(
            onSuccess = onSuccess,
            onError = { }
        )

        verify { onSuccess("data") }
    }

    @Test
    fun `UseCaseResult doOnResult - on error`() {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)
        val onError = spyk<(ErrorResult) -> Unit>()

        useCaseResult.doOnResult(
            onSuccess = { },
            onError = onError
        )

        verify { onError(errorResult) }
    }

    @Test
    fun `UseCaseResult onSuccess - on success`() {
        val useCaseResult = UseCaseResult.Success("data")
        val onSuccess = spyk<(String) -> Unit>()

        useCaseResult.onSuccess(onSuccess)

        verify { onSuccess("data") }
    }

    @Test
    fun `UseCaseResult onSuccess - on error`() {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)
        val onSuccess = spyk<(String) -> Unit>()

        useCaseResult.onSuccess(onSuccess)

        verify(exactly = 0) { onSuccess("data") }
    }

    @Test
    fun `UseCaseResult map - on success`() = runTest {
        val useCaseResult = UseCaseResult.Success(1)
        val mapFunction: (Int) -> String = { it.toString() }

        val result = useCaseResult.map { mapFunction(it) }

        assertEquals(UseCaseResult.Success("1"), result)
    }

    @Test
    fun `UseCaseResult map - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)

        val result = useCaseResult.map { }

        assertEquals(useCaseResult, result)
    }

    @Test
    fun `UseCaseResult flatMap - on success`() = runTest {
        val useCaseResult = UseCaseResult.Success(1)

        val result = useCaseResult.flatMap { UseCaseResult.Success(it + 1) }

        assertEquals(UseCaseResult.Success(2), result)
    }

    @Test
    fun `UseCaseResult flatMap - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult: UseCaseResult<Int> = UseCaseResult.Error(errorResult)

        val result = useCaseResult.flatMap { UseCaseResult.Success(it + 1) }

        assertEquals(useCaseResult, result)
    }

    @Test
    fun `UseCaseResult combine - extension with lambda - on success`() = runTest {
        val useCaseResult = UseCaseResult.Success(1)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combine(
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Success("1 data"), result)
    }

    @Test
    fun `UseCaseResult combine - extension with lambda - on first error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combine(
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combine - extension with lambda - on second error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Success("data")
        val secondUseCaseResult = UseCaseResult.Error(errorResult)

        val result = useCaseResult.combine(
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combineIf - extension with lambda - on success with condition true`() = runTest {
        val useCaseResult = UseCaseResult.Success(1)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combineIf(
            condition = true,
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            },
            notCombine = { "$it" }
        )

        assertEquals(UseCaseResult.Success("1 data"), result)
    }

    @Test
    fun `UseCaseResult combineIf - extension with lambda - on success with condition false`() = runTest {
        val useCaseResult = UseCaseResult.Success(1)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combineIf(
            condition = false,
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            },
            notCombine = { "$it" }
        )

        assertEquals(UseCaseResult.Success("1"), result)
    }

    @Test
    fun `UseCaseResult combineIf - extension with lambda - on first error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combineIf(
            condition = true,
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            },
            notCombine = { "$it" }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combineIf - extension with lambda - on second error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Success("data")
        val secondUseCaseResult = UseCaseResult.Error(errorResult)

        val result = useCaseResult.combineIf(
            condition = true,
            secondResult = {
                secondUseCaseResult
            },
            combine = { first, second ->
                "$first $second"
            },
            notCombine = { it }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combine - extension with result - on first error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)
        val secondUseCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.combine(
            secondResult = secondUseCaseResult,
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combine - extension with result - on second error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Success("data")
        val secondUseCaseResult = UseCaseResult.Error(errorResult)

        val result = useCaseResult.combine(
            secondResult = secondUseCaseResult,
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Error(errorResult), result)
    }

    @Test
    fun `UseCaseResult combine method`() = runTest {
        val firstCall = { UseCaseResult.Success("data1") }
        val secondCall = { UseCaseResult.Success("data2") }

        val result = combine(
            first = firstCall,
            second = secondCall,
            combine = { first, second ->
                "$first $second"
            }
        )

        assertEquals(UseCaseResult.Success("data1 data2"), result)
    }

    @Test
    fun `UseCaseResult asResult - on success`() = runTest {
        val useCaseResult = UseCaseResult.Success("data")

        val result = useCaseResult.asResult()

        assertTrue(result.isSuccess)
        assertEquals("data", result.getOrThrow())
    }

    @Test
    fun `UseCaseResult asResult - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val useCaseResult = UseCaseResult.Error(errorResult)

        val result = useCaseResult.asResult()

        assertTrue(result.isFailure)
        assertEquals(errorResult, result.exceptionOrNull())
    }
}
