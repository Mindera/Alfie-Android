package au.com.alfie.ecomm.repository.result

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RepositoryResultTest {

    @Test
    fun `RepositoryResult flatMap - on success`() = runTest {
        val repositoryResult = RepositoryResult.Success(1)

        val result = repositoryResult.flatMap { RepositoryResult.Success(it + 1) }

        assertEquals(RepositoryResult.Success(2), result)
    }

    @Test
    fun `RepositoryResult flatMap - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val repositoryResult: RepositoryResult<Int> = RepositoryResult.Error(errorResult)

        val result = repositoryResult.flatMap { RepositoryResult.Success(it + 1) }

        assertEquals(repositoryResult, result)
    }

    @Test
    fun `RepositoryResult map - on success`() = runTest {
        val repositoryResult = RepositoryResult.Success(1)

        val result = repositoryResult.map { it + 1 }

        assertEquals(RepositoryResult.Success(2), result)
    }

    @Test
    fun `RepositoryResult map - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val repositoryResult: RepositoryResult<Int> = RepositoryResult.Error(errorResult)

        val result = repositoryResult.map { it + 1 }

        assertEquals(repositoryResult, result)
    }

    @Test
    fun `RepositoryResult onSuccess - on success`() = runTest {
        val repositoryResult = RepositoryResult.Success(1)
        val mockLambda = mockk<() -> Unit>(relaxed = true)

        val result = repositoryResult.onSuccess { mockLambda() }

        assertEquals(repositoryResult, result)
        verify { mockLambda() }
    }

    @Test
    fun `RepositoryResult onSuccess - on error`() = runTest {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val repositoryResult: RepositoryResult<Int> = RepositoryResult.Error(errorResult)
        val mockLambda = mockk<() -> Unit>(relaxed = true)

        val result = repositoryResult.onSuccess { mockLambda() }

        assertEquals(repositoryResult, result)
        verify(exactly = 0) { mockLambda() }
    }

    @Test
    fun `RepositoryResult fold - on success`() {
        val repositoryResult = RepositoryResult.Success(1)

        val result = repositoryResult.fold(
            onSuccess = { "Success $it" },
            onError = { "Error" }
        )

        assertEquals("Success 1", result)
    }

    @Test
    fun `RepositoryResult fold - on error`() {
        val errorResult = ErrorResult(ErrorType.GENERIC_ERROR)
        val repositoryResult: RepositoryResult<Int> = RepositoryResult.Error(errorResult)

        val result = repositoryResult.fold(
            onSuccess = { "Success $it" },
            onError = { "Error" }
        )

        assertEquals("Error", result)
    }
}
