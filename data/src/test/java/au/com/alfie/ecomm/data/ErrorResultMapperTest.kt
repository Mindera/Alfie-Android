package au.com.alfie.ecomm.data

import au.com.alfie.ecomm.network.exception.GraphNetworkException.BadRequestException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ClientException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ConflictException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.InvalidResponseException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.MethodNotAllowedException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NotFoundException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ServerException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnProcessableEntityException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnauthorizedException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnexpectedException
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType.AUTHENTICATION_FAILED
import au.com.alfie.ecomm.repository.result.ErrorType.BAD_REQUEST
import au.com.alfie.ecomm.repository.result.ErrorType.CONFLICT
import au.com.alfie.ecomm.repository.result.ErrorType.GENERIC_ERROR
import au.com.alfie.ecomm.repository.result.ErrorType.INVALID_REQUEST
import au.com.alfie.ecomm.repository.result.ErrorType.METHOD_NOT_ALLOWED
import au.com.alfie.ecomm.repository.result.ErrorType.NETWORK
import au.com.alfie.ecomm.repository.result.ErrorType.RESOURCE_NOT_FOUND
import au.com.alfie.ecomm.repository.result.ErrorType.UNKNOWN
import au.com.alfie.ecomm.repository.result.ErrorType.UN_PROCESSABLE_ENTITY
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ErrorResultMapperTest {

    @Test
    fun `WHEN BadRequestException THEN ErrorResult should be of type BAD_REQUEST`() = runTest {
        val exception = BadRequestException(
            message = "message"
        )
        val expected = ErrorResult(
            type = BAD_REQUEST,
            code = "400",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN UnauthorizedException THEN ErrorResult should be of type AUTHENTICATION_FAILED`() = runTest {
        val exception = UnauthorizedException(
            message = "message"
        )
        val expected = ErrorResult(
            type = AUTHENTICATION_FAILED,
            code = "401",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN NotFoundException THEN ErrorResult should be of type RESOURCE_NOT_FOUND`() = runTest {
        val exception = NotFoundException(
            message = "message"
        )
        val expected = ErrorResult(
            type = RESOURCE_NOT_FOUND,
            code = "404",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN MethodNotAllowedException THEN ErrorResult should be of type RESOURCE_NOT_FOUND`() = runTest {
        val exception = MethodNotAllowedException(
            message = "message"
        )
        val expected = ErrorResult(
            type = METHOD_NOT_ALLOWED,
            code = "405",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN ConflictException THEN ErrorResult should be of type CONFLICT`() = runTest {
        val exception = ConflictException(
            message = "message"
        )
        val expected = ErrorResult(
            type = CONFLICT,
            code = "409",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN UnProcessableEntityException THEN ErrorResult should be of type UN_PROCESSABLE_ENTITY`() = runTest {
        val exception = UnProcessableEntityException(
            message = "message"
        )
        val expected = ErrorResult(
            type = UN_PROCESSABLE_ENTITY,
            code = "422",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN ClientException THEN ErrorResult should be of type GENERIC_ERROR`() = runTest {
        val exception = ClientException(
            code = 499,
            message = "message"
        )
        val expected = ErrorResult(
            type = GENERIC_ERROR,
            code = "499",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN ServerException THEN ErrorResult should be of type GENERIC_ERROR`() = runTest {
        val exception = ServerException(
            code = 599,
            message = "message"
        )
        val expected = ErrorResult(
            type = GENERIC_ERROR,
            code = "599",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN NetworkException THEN ErrorResult should be of type NETWORK`() = runTest {
        val exception = NetworkException(
            code = 123,
            message = "message"
        )
        val expected = ErrorResult(
            type = NETWORK,
            code = "123",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN InvalidResponseException THEN ErrorResult should be of type INVALID_REQUEST`() = runTest {
        val exception = InvalidResponseException(
            message = "message"
        )
        val expected = ErrorResult(
            type = INVALID_REQUEST,
            code = "0",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN UnexpectedException THEN ErrorResult should be of type UNKNOWN`() = runTest {
        val exception = UnexpectedException(
            message = "message"
        )
        val expected = ErrorResult(
            type = UNKNOWN,
            code = "0",
            errorMessage = "message"
        )

        val result = exception.toErrorResult()
        assertEquals(expected, result)
    }
}
