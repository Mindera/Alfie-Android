package com.mindera.alfie.network.interceptor

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MockKExtension::class)
internal class RetryInterceptorTest {

    @MockK
    private lateinit var chain: Interceptor.Chain

    private lateinit var interceptor: RetryInterceptor

    private val dummyRequest = Request.Builder().url("https://example.com").build()

    @BeforeEach
    fun setUp() {
        interceptor = RetryInterceptor().also { it.delayFn = { /* no-op in tests */ } }
        every { chain.request() } returns dummyRequest
    }

    @Test
    fun `WHEN response is 200 THEN return immediately without retry`() {
        every { chain.proceed(any()) } answers { buildResponse(200) }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        verify(exactly = 1) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN response is 429 THEN retry up to MAX_ATTEMPTS and return last response`() {
        every { chain.proceed(any()) } answers { buildResponse(429) }

        val result = interceptor.intercept(chain)

        assertEquals(429, result.code)
        verify(exactly = 3) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN response is 430 THEN retry up to MAX_ATTEMPTS`() {
        every { chain.proceed(any()) } answers { buildResponse(430) }

        interceptor.intercept(chain)

        verify(exactly = 3) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN response is 500 THEN retry up to MAX_ATTEMPTS`() {
        every { chain.proceed(any()) } answers { buildResponse(500) }

        interceptor.intercept(chain)

        verify(exactly = 3) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN response is 404 THEN return immediately without retry`() {
        every { chain.proceed(any()) } answers { buildResponse(404) }

        val result = interceptor.intercept(chain)

        assertEquals(404, result.code)
        verify(exactly = 1) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN first attempt throws IOException and second succeeds THEN return success`() {
        every { chain.proceed(any()) } throws IOException("timeout") andThenAnswer { buildResponse(200) }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        verify(exactly = 2) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN all attempts throw IOException THEN rethrow exception`() {
        every { chain.proceed(any()) } throws IOException("network error")

        assertFailsWith<IOException> {
            interceptor.intercept(chain)
        }
        verify(exactly = 3) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN 429 recovers on second attempt THEN return success`() {
        every { chain.proceed(any()) } answers { buildResponse(429) } andThenAnswer { buildResponse(200) }

        val result = interceptor.intercept(chain)

        assertEquals(200, result.code)
        verify(exactly = 2) { chain.proceed(any()) }
    }

    @Test
    fun `WHEN Retry-After header is present THEN use it as delay`() {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }

        every { chain.proceed(any()) } answers { buildResponse(429, retryAfterSeconds = 5) } andThenAnswer { buildResponse(200) }

        interceptor.intercept(chain)

        assertEquals(1, delays.size)
        assertEquals(5.seconds, delays.first())
    }

    @Test
    fun `WHEN Retry-After header is zero THEN delay is clamped to MIN_DELAY`() {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }
        every { chain.proceed(any()) } answers { buildResponse(429, retryAfterSeconds = 0) } andThenAnswer { buildResponse(200) }

        interceptor.intercept(chain)

        assertEquals(1, delays.size)
        assertEquals(100.milliseconds, delays.first())
    }

    @Test
    fun `WHEN Retry-After header exceeds MAX_DELAY THEN delay is clamped to MAX_DELAY`() {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }
        every { chain.proceed(any()) } answers { buildResponse(429, retryAfterSeconds = 9999) } andThenAnswer { buildResponse(200) }

        interceptor.intercept(chain)

        assertEquals(1, delays.size)
        assertEquals(30.seconds, delays.first())
    }

    @Test
    fun `WHEN multiple retries occur THEN delays follow exponential backoff progression`() {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }
        every { chain.proceed(any()) } answers { buildResponse(500) }

        interceptor.intercept(chain)

        // 3 attempts → 2 inter-attempt delays
        assertEquals(2, delays.size)
        assertTrue(delays[1] > delays[0], "Second delay (${delays[1]}) should be greater than first (${delays[0]})")
    }

    @Test
    fun `WHEN computing backoff delays THEN jitter keeps them within 20 percent bounds`() {
        val delays = mutableListOf<Duration>()
        interceptor.delayFn = { delays.add(it) }
        every { chain.proceed(any()) } answers { buildResponse(500) }

        interceptor.intercept(chain)

        // Attempt 1: base=1s, jitter ±20% → [0.8s, 1.2s]
        assertTrue(delays[0] >= 0.8.seconds && delays[0] <= 1.2.seconds, "First delay ${delays[0]} should be within [0.8s, 1.2s]")
        // Attempt 2: base=2s, jitter ±20% → [1.6s, 2.4s]
        assertTrue(delays[1] >= 1.6.seconds && delays[1] <= 2.4.seconds, "Second delay ${delays[1]} should be within [1.6s, 2.4s]")
    }

    private fun buildResponse(code: Int, retryAfterSeconds: Long? = null): Response {
        val builder = Response.Builder()
            .request(dummyRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("")
            .body("".toResponseBody("text/plain".toMediaType()))
        retryAfterSeconds?.let { builder.header("Retry-After", it.toString()) }
        return builder.build()
    }
}
