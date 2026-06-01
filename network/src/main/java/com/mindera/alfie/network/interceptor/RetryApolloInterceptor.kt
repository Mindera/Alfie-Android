package com.mindera.alfie.network.interceptor

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class RetryApolloInterceptor @Inject constructor() : ApolloInterceptor {

    // Overridable in tests to skip actual delays
    internal var delayFn: suspend (Duration) -> Unit = { delay(it) }

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> {
        if (request.operation is Mutation<*>) return chain.proceed(request)

        return flow {
            var attempt = 0
            while (true) {
                attempt++
                val response = chain.proceed(request).first()
                val httpException = response.exception as? ApolloHttpException
                val isRetryable = httpException != null && shouldRetry(httpException.statusCode) ||
                    response.exception is ApolloNetworkException

                if (!isRetryable || attempt >= MAX_ATTEMPTS) {
                    emit(response)
                    break
                }

                delayFn(retryDelay(attempt, httpException))
            }
        }
    }

    private fun shouldRetry(code: Int): Boolean =
        code == HTTP_TOO_MANY_REQUESTS ||
            code == HTTP_TOO_MANY_REQUESTS_EXTENDED ||
            code in HTTP_5XX_RANGE

    private fun retryDelay(attempt: Int, httpException: ApolloHttpException?): Duration {
        val retryAfter = httpException?.headers
            ?.firstOrNull { it.name.equals(RETRY_AFTER_HEADER, ignoreCase = true) }
            ?.value
            ?.toLongOrNull()
            ?.seconds
            ?.coerceIn(MIN_DELAY, MAX_DELAY)
        if (retryAfter != null) return retryAfter

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
