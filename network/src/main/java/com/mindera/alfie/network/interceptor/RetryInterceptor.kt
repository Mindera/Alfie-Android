package com.mindera.alfie.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class RetryInterceptor @Inject constructor() : Interceptor {

    // Overridable in tests to skip actual sleeping
    internal var delayFn: (Duration) -> Unit = { Thread.sleep(it.inWholeMilliseconds) }

    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        while (true) {
            attempt++
            try {
                val response = chain.proceed(chain.request())

                if (!shouldRetry(response.code) || attempt >= MAX_ATTEMPTS) {
                    return response
                }

                response.close()
                delayFn(retryDelay(attempt, response))
            } catch (e: IOException) {
                if (attempt >= MAX_ATTEMPTS) throw e
                delayFn(retryDelay(attempt, null))
            }
        }
    }

    private fun shouldRetry(code: Int): Boolean =
        code == HTTP_TOO_MANY_REQUESTS ||
            code == HTTP_TOO_MANY_REQUESTS_EXTENDED ||
            code in HTTP_5XX_RANGE

    private fun retryDelay(attempt: Int, response: Response?): Duration {
        // Honour Retry-After header when present (seconds as integer)
        val retryAfter = response?.header(RETRY_AFTER_HEADER)
            ?.toLongOrNull()
            ?.seconds
        if (retryAfter != null) return retryAfter

        // Exponential backoff: 1s, 2s, 4s… capped at 30s, ±20% jitter
        val base = BASE_DELAY * (1 shl (attempt - 1))
        val capped = base.coerceAtMost(MAX_DELAY)
        val jitter = capped * (JITTER_FACTOR * (Random.nextDouble() * 2 - 1))
        return (capped + jitter).coerceAtLeast(MIN_DELAY)
    }

    private companion object {
        const val MAX_ATTEMPTS = 3
        val BASE_DELAY = 1.seconds
        val MAX_DELAY = 30.seconds
        val MIN_DELAY = 100.milliseconds
        const val JITTER_FACTOR = 0.2
        const val RETRY_AFTER_HEADER = "Retry-After"
        const val HTTP_TOO_MANY_REQUESTS = 429
        const val HTTP_TOO_MANY_REQUESTS_EXTENDED = 430
        val HTTP_5XX_RANGE = 500..599
    }
}
