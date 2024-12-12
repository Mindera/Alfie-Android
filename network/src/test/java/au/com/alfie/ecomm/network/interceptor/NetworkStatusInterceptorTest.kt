package au.com.alfie.ecomm.network.interceptor

import app.cash.turbine.test
import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.INTERNAL_HTTP_ERROR
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.network.util.ConnectionManager
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.benasher44.uuid.Uuid
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
internal class NetworkStatusInterceptorTest {

    @RelaxedMockK
    lateinit var connectionManager: ConnectionManager

    @InjectMockKs
    lateinit var subject: NetworkStatusInterceptor

    @RelaxedMockK
    lateinit var request: ApolloRequest<Operation.Data>

    @RelaxedMockK
    lateinit var chain: ApolloInterceptorChain

    @Test
    fun `WHEN device has connection THEN request proceeds`() = runTest {
        val expected = ApolloResponse.Builder(
            operation = mockk<Operation<Operation.Data>>(),
            requestUuid = mockk<Uuid>()
        ).build()

        every { connectionManager.isConnected } returns true
        every { chain.proceed(request) } returns flowOf(expected)

        val result = subject.intercept(request, chain)

        result.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `WHEN device does not have connection THEN throws exception`() = runTest {
        val expected = NetworkException(
            code = INTERNAL_HTTP_ERROR.code,
            message = "No internet connection"
        )
        val response = ApolloResponse.Builder(
            operation = mockk<Operation<Operation.Data>>(),
            requestUuid = mockk<Uuid>()
        ).build()

        every { connectionManager.isConnected } returns false
        every { chain.proceed(request) } returns flowOf(response)

        val result = subject.intercept(request, chain)

        result.test {
            val error = awaitError()
            assertIs<NetworkException>(error)
            assertEquals(expected, error)
        }
    }
}
