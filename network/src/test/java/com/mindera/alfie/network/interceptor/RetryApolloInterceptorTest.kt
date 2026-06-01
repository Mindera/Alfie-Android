package com.mindera.alfie.network.interceptor

import app.cash.turbine.test
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.http.HttpHeader
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.benasher44.uuid.Uuid
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MockKExtension::class)
internal class RetryApolloInterceptorTest {

    @RelaxedMockK
    lateinit var chain: ApolloInterceptorChain

    @RelaxedMockK
    lateinit var request: ApolloRequest<Operation.Data>

    private lateinit var interceptor: RetryApolloInterceptor

    @BeforeEach
    fun setUp() {
        interceptor = RetryApolloInterceptor().also { it.delayFn = { /* no-op in tests */ } }
        every { request.operation } returns mockk<Operation<Operation.Data>>()
    }

    // region: non-retryable cases

    @Test
    fun `WHEN response is 200 THEN return immediately without retry`() = runTest {
        val response = buildResponse()
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            assertEquals(response, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `WHEN response has no exception THEN return immediately without retry`() = runTest {
        val response = buildResponse()
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `WHEN response is 404 THEN return immediately without retry`() = runTest {
        val response = buildResponse(httpException(404))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            assertEquals(response, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { chain.proceed(request) }
    }

    // region: mutation passthrough

    @Test
    fun `WHEN operation is Mutation THEN skip retry logic entirely`() = runTest {
        @Suppress("UNCHECKED_CAST")
        every { request.operation } returns (mockk<Mutation<Mutation.Data>>(relaxed = true) as Operation<Operation.Data>)
        val response = buildResponse(httpException(500))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            assertEquals(response, awaitItem())
            awaitComplete()
        }
        // chain.proceed called exactly once — no retries
        verify(exactly = 1) { chain.proceed(request) }
    }

    // region: retryable HTTP status codes

    @Test
    fun `WHEN response is 429 THEN retry up to MAX_ATTEMPTS and return last response`() = runTest {
        val response = buildResponse(httpException(429))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            assertEquals(response, awaitItem())
            awaitComplete()
        }
        verify(exactly = 3) { chain.proceed(request) }
    }

    @Test
    fun `WHEN response is 430 THEN retry up to MAX_ATTEMPTS`() = runTest {
        val response = buildResponse(httpException(430))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        verify(exactly = 3) { chain.proceed(request) }
    }

    @Test
    fun `WHEN response is 500 THEN retry up to MAX_ATTEMPTS`() = runTest {
        val response = buildResponse(httpException(500))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        verify(exactly = 3) { chain.proceed(request) }
    }

    @Test
    fun `WHEN 429 recovers on second attempt THEN return success and stop retrying`() = runTest {
        val errorResponse = buildResponse(httpException(429))
        val successResponse = buildResponse()
        every { chain.proceed(request) } returnsMany listOf(
            flowOf(errorResponse),
            flowOf(successResponse)
        )

        interceptor.intercept(request, chain).test {
            assertEquals(successResponse, awaitItem())
            awaitComplete()
        }
        verify(exactly = 2) { chain.proceed(request) }
    }

    // region: network errors

    @Test
    fun `WHEN response has ApolloNetworkException THEN retry up to MAX_ATTEMPTS`() = runTest {
        val response = buildResponse(ApolloNetworkException("network error"))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        verify(exactly = 3) { chain.proceed(request) }
    }

    @Test
    fun `WHEN network error recovers on second attempt THEN return success`() = runTest {
        val errorResponse = buildResponse(ApolloNetworkException("network error"))
        val successResponse = buildResponse()
        every { chain.proceed(request) } returnsMany listOf(
            flowOf(errorResponse),
            flowOf(successResponse)
        )

        interceptor.intercept(request, chain).test {
            assertEquals(successResponse, awaitItem())
            awaitComplete()
        }
        verify(exactly = 2) { chain.proceed(request) }
    }

    // region: Retry-After header

    @Test
    fun `WHEN Retry-After header is present THEN use it as delay`() = runTest {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        val errorResponse = buildResponse(httpException(429, retryAfterSeconds = 5))
        val successResponse = buildResponse()
        every { chain.proceed(request) } returnsMany listOf(
            flowOf(errorResponse),
            flowOf(successResponse)
        )

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        assertEquals(1, delays.size)
        assertEquals(5.seconds, delays.first())
    }

    @Test
    fun `WHEN Retry-After header is zero THEN delay is clamped to MIN_DELAY`() = runTest {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        val errorResponse = buildResponse(httpException(429, retryAfterSeconds = 0))
        val successResponse = buildResponse()
        every { chain.proceed(request) } returnsMany listOf(
            flowOf(errorResponse),
            flowOf(successResponse)
        )

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        assertEquals(1, delays.size)
        assertEquals(100.milliseconds, delays.first())
    }

    @Test
    fun `WHEN Retry-After header exceeds MAX_DELAY THEN delay is clamped to MAX_DELAY`() = runTest {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        val errorResponse = buildResponse(httpException(429, retryAfterSeconds = 9999))
        val successResponse = buildResponse()
        every { chain.proceed(request) } returnsMany listOf(
            flowOf(errorResponse),
            flowOf(successResponse)
        )

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        assertEquals(1, delays.size)
        assertEquals(30.seconds, delays.first())
    }

    // region: backoff and jitter

    @Test
    fun `WHEN multiple retries occur THEN delays follow exponential backoff progression`() = runTest {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        val response = buildResponse(httpException(500))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        // 3 attempts → 2 inter-attempt delays
        assertEquals(2, delays.size)
        assertTrue(delays[1] > delays[0], "Second delay (${delays[1]}) should be greater than first (${delays[0]})")
    }

    @Test
    fun `WHEN computing backoff delays THEN jitter keeps them within 20 percent bounds`() = runTest {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        val response = buildResponse(httpException(500))
        every { chain.proceed(request) } returns flowOf(response)

        interceptor.intercept(request, chain).test {
            awaitItem()
            awaitComplete()
        }
        // Attempt 1: base=1s, jitter ±20% → [0.8s, 1.2s]
        assertTrue(delays[0] >= 0.8.seconds && delays[0] <= 1.2.seconds, "First delay ${delays[0]} should be within [0.8s, 1.2s]")
        // Attempt 2: base=2s, jitter ±20% → [1.6s, 2.4s]
        assertTrue(delays[1] >= 1.6.seconds && delays[1] <= 2.4.seconds, "Second delay ${delays[1]} should be within [1.6s, 2.4s]")
    }

    // region: helpers

    private fun buildResponse(exception: ApolloException? = null): ApolloResponse<Operation.Data> {
        val builder = ApolloResponse.Builder(
            operation = mockk<Operation<Operation.Data>>(),
            requestUuid = mockk<Uuid>()
        )
        exception?.let { builder.exception(it) }
        return builder.build()
    }

    private fun httpException(statusCode: Int, retryAfterSeconds: Long? = null): ApolloHttpException {
        val headers = retryAfterSeconds?.let { listOf(HttpHeader("Retry-After", it.toString())) } ?: emptyList()
        return ApolloHttpException(
            statusCode = statusCode,
            headers = headers,
            body = null,
            message = "HTTP $statusCode"
        )
    }
}
