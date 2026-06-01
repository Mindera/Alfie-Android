package com.mindera.alfie.network.extension

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.exception.ApolloHttpException
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_BAD_REQUEST
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_CONFLICT
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_NOT_FOUND
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UNAUTHORIZED
import com.mindera.alfie.network.exception.ExceptionErrorCodes.HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT
import com.mindera.alfie.network.exception.GraphNetworkException.BadRequestException
import com.mindera.alfie.network.exception.GraphNetworkException.ConflictException
import com.mindera.alfie.network.exception.GraphNetworkException.MethodNotAllowedException
import com.mindera.alfie.network.exception.GraphNetworkException.NetworkException
import com.mindera.alfie.network.exception.GraphNetworkException.NotFoundException
import com.mindera.alfie.network.exception.GraphNetworkException.ThrottledException
import com.mindera.alfie.network.exception.GraphNetworkException.TimeoutException
import com.mindera.alfie.network.exception.GraphNetworkException.UnProcessableEntityException
import com.mindera.alfie.network.exception.GraphNetworkException.UnauthorizedException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.SocketTimeoutException
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

    @Test
    fun `WHEN response has error 429 exception THEN throw ThrottledException`() = runTest {
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS.code,
            headers = listOf(),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is ThrottledException)
    }

    @Test
    fun `WHEN response has error 429 with Retry-After header THEN ThrottledException carries retryAfter`() = runTest {
        val retryAfterHeader = com.apollographql.apollo.api.http.HttpHeader("Retry-After", "60")
        val exception = ApolloHttpException(
            statusCode = HTTP_CLIENT_ERROR_TOO_MANY_REQUESTS.code,
            headers = listOf(retryAfterHeader),
            message = "Unit test",
            body = null
        )
        apolloResponse.setPrivatePropertyField("exception", exception)

        coEvery { apolloCall.execute() } returns apolloResponse

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        val throttled = result.exceptionOrNull() as? ThrottledException
        assert(throttled?.retryAfter != null)
        assert(throttled?.retryAfter?.inWholeSeconds == 60L)
    }

    @Test
    fun `WHEN SocketTimeoutException is thrown THEN unwrap returns TimeoutException`() = runTest {
        coEvery { apolloCall.execute() } throws SocketTimeoutException("timeout")

        val result = apolloCall.unwrap()

        assert(result.isFailure)
        assert(result.exceptionOrNull() is TimeoutException)
    }
}
