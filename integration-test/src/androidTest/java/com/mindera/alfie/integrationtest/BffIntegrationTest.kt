package com.mindera.alfie.integrationtest

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import java.util.concurrent.TimeUnit

/**
 * Base for integration tests that hit a **locally-running BFF** with a real [ApolloClient].
 *
 * These are instrumented tests ([connectedDebugAndroidTest]); from an emulator the host's
 * `localhost` is reached via [BFF_URL] (`10.0.2.2`). Cleartext to `10.0.2.2` is permitted by
 * `app/src/main/res/xml/network_security_config.xml`.
 *
 * Each test is gated on BFF reachability ([assumeBffReachable]); when no BFF is up the test is
 * **skipped, not failed**, so the suite stays green on devices/CI without a backend. See the
 * README "Integration tests" section for how to start the BFF.
 */
internal abstract class BffIntegrationTest {

    @BeforeEach
    fun assumeBffReachable() {
        assumeTrue(
            isBffReachable(),
            "BFF not reachable at $BFF_URL — skipping. Start the BFF locally (see README)."
        )
    }

    /** Builds a real client, runs [block] in a blocking coroutine, and always closes the client. */
    protected fun <T> withBff(block: suspend (ApolloClient) -> T): T {
        val client = ApolloClient.Builder().serverUrl(BFF_URL).build()
        return try {
            runBlocking { block(client) }
        } finally {
            client.close()
        }
    }

    /** Executes [query] and returns its data, throwing if the response has errors or null data. */
    protected suspend fun <D : Query.Data> ApolloClient.executeOrFail(query: Query<D>): D =
        query(query).execute().dataAssertNoErrors

    private fun isBffReachable(): Boolean = runCatching {
        val client = OkHttpClient.Builder()
            .callTimeout(READINESS_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        val body = """{"query":"{ __typename }"}""".toRequestBody(JSON_MEDIA_TYPE.toMediaType())
        client.newCall(Request.Builder().url(BFF_URL).post(body).build())
            .execute()
            .use { it.isSuccessful }
    }.onFailure { android.util.Log.w("BffIntegration", "readiness check failed for $BFF_URL", it) }
        .getOrDefault(false)

    companion object {
        /** Emulator → host alias for the local BFF (NestJS, port 3000, `/graphql`). */
        const val BFF_URL = "http://10.0.2.2:3000/graphql"

        /**
         * The local BFF is configured for a single platform per deployment. Mirrors the app's
         * default (`com.mindera.alfie.repository.product.Platforms.SHOPIFY`).
         */
        const val PLATFORM = "shopify"

        /** A Shopify catch-all collection handle, used as the default Store collection. */
        const val DEFAULT_COLLECTION_HANDLE = "frontpage"

        private const val READINESS_TIMEOUT_SECONDS = 3L
        private const val JSON_MEDIA_TYPE = "application/json"
    }
}
