package au.com.alfie.ecomm.data

import au.com.alfie.ecomm.network.exception.GraphNetworkException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType.NETWORK
import au.com.alfie.ecomm.repository.result.ErrorType.UNKNOWN
import au.com.alfie.ecomm.repository.result.RepositoryResult
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class RepositoryResultMapperTest {

    @Test
    fun `WHEN network response is valid THEN Success is returned`() = runTest {
        val data = "Unit Test"
        val networkResponse = Result.success(data)

        val expected = RepositoryResult.Success(data)
        val result = networkResponse.toRepositoryResult()

        assertEquals(expected, result)
    }

    @Test
    fun `WHEN network response is GraphNetworkException THEN ErrorResult is returned`() = runTest {
        val exception = NetworkException(
            code = 0,
            message = "Unit Test"
        )
        val networkResponse = Result.failure<GraphNetworkException>(exception)

        val expected = RepositoryResult.Error(
            ErrorResult(
                type = NETWORK,
                errorMessage = "Unit Test",
                code = "0"
            )
        )

        val result = networkResponse.toRepositoryResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN network response is generic Exception THEN Unknown ErrorResult is returned`() = runTest {
        val exception = Exception()
        val networkResponse = Result.failure<Exception>(exception)

        val expected = RepositoryResult.Error(
            ErrorResult(type = UNKNOWN)
        )

        val result = networkResponse.toRepositoryResult()
        assertEquals(expected, result)
    }
}
