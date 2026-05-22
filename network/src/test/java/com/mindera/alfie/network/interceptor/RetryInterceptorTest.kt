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
import kotlin.time.Duration
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
