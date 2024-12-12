package au.com.alfie.ecomm.network.extension

import au.com.alfie.ecomm.core.test.setPrivatePropertyField
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_BAD_REQUEST
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_CONFLICT
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_NOT_FOUND
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UNAUTHORIZED
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT
import au.com.alfie.ecomm.network.exception.GraphNetworkException.BadRequestException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.ConflictException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.MethodNotAllowedException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NotFoundException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnProcessableEntityException
import au.com.alfie.ecomm.network.exception.GraphNetworkException.UnauthorizedException
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloHttpException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ApolloCallExtTest {

    @RelaxedMockK
    lateinit var apolloResponse: ApolloResponse<Operation.Data>

    @RelaxedMockK
    lateinit var apolloCall: ApolloCall<Operation.Data>

    @Test
    fun `WHEN response is success THEN unwrap data`() = runTest {
        apolloResponse.setPrivatePropertyField("data", mockk<Operation.Data>())
        every { apolloResponse.hasErrors() } returns false
        coEvery { apolloCall.execute() } returns apolloResponse

        val expected = Result.success(apolloResponse.dataAssertNoErrors)

        val result = apolloCall.unwrap()

        verify { apolloResponse.dataAssertNoErrors }
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN response has errors THEN throw NetworkException exception`() = runTest {
        every { apolloResponse.hasErrors() } returns true
        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is NetworkException)
    }

    @Test
    fun `WHEN response has error 400 exception THEN throw BadRequestException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_BAD_REQUEST.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is BadRequestException)
    }

    @Test
    fun `WHEN response has error 401 exception THEN throw UnauthorizedException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_UNAUTHORIZED.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is UnauthorizedException)
    }

    @Test
    fun `WHEN response has error 404 exception THEN throw NotFoundException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_NOT_FOUND.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is NotFoundException)
    }

    @Test
    fun `WHEN response has error 405 exception THEN throw MethodNotAllowedException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is MethodNotAllowedException)
    }

    @Test
    fun `WHEN response has error 409 exception THEN throw ConflictException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_CONFLICT.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is ConflictException)
    }

    @Test
    fun `WHEN response has error 422 exception THEN throw UnProcessableEntityException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is UnProcessableEntityException)
    }
}
