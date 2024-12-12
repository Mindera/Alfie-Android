package au.com.alfie.ecomm.domain

import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UseCaseInteractorTest {

    @Test
    fun `run - successful RepositoryResult return successful UseCaseResult`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()

        val result = interactor.run {
            run(RepositoryResult.Success("something"))
        }

        assertIs<UseCaseResult.Success<String>>(result)
        assertEquals("something", result.data)
    }

    @Test
    fun `run - error RepositoryResult return error UseCaseResult`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()

        val error = mockk<ErrorResult>()
        val result = interactor.run {
            run(RepositoryResult.Error(error))
        }

        assertIs<UseCaseResult.Error>(result)
        assertEquals(error, result.error)
    }

    @Test
    fun `runMap - successful RepositoryResult return successful UseCaseResult calling map`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()
        val mapper: suspend (String) -> String = mockk()
        coEvery { mapper(any()) } returns "something2"

        val result = interactor.run {
            runMap(
                response = RepositoryResult.Success("something"),
                mapUseCase = mapper
            )
        }

        assertIs<UseCaseResult.Success<String>>(result)
        assertEquals("something2", result.data)
        coVerify { mapper(any()) }
    }

    @Test
    fun `runMap - error RepositoryResult return error UseCaseResult`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()

        val error = mockk<ErrorResult>()
        val result = interactor.run {
            runMap(
                response = RepositoryResult.Error(error),
                mapUseCase = {}
            )
        }

        assertIs<UseCaseResult.Error>(result)
        assertEquals(error, result.error)
    }

    @Test
    fun `runMapResult - successful RepositoryResult return successful UseCaseResult calling map`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()
        val mapper: suspend (String) -> UseCaseResult<String> = mockk()
        coEvery { mapper(any()) } returns UseCaseResult.Success("something2")

        val result = interactor.run {
            runMapResult(
                response = RepositoryResult.Success("something"),
                mapUseCase = mapper
            )
        }

        assertIs<UseCaseResult.Success<String>>(result)
        assertEquals("something2", result.data)
        coVerify { mapper(any()) }
    }

    @Test
    fun `runMapResult - error RepositoryResult return error UseCaseResult`() = runTest {
        val interactor: UseCaseInteractor = TestUseCaseInteractor()

        val error = mockk<ErrorResult>()
        val result = interactor.run {
            runMapResult(
                response = RepositoryResult.Error(error),
                mapUseCase = { mockk<UseCaseResult.Success<String>>() }
            )
        }

        assertIs<UseCaseResult.Error>(result)
        assertEquals(error, result.error)
    }

    private class TestUseCaseInteractor : UseCaseInteractor
}
