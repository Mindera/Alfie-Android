package com.mindera.alfie.network.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.LoggingInterceptor
import com.apollographql.apollo.network.http.LoggingInterceptor.Level.BODY
import com.apollographql.apollo.network.okHttpClient
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.debug.interceptor.DebugInterceptors
import com.mindera.alfie.network.interceptor.NetworkStatusInterceptor
import com.mindera.alfie.network.interceptor.RetryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
internal object ApolloModule {

    private val CONNECT_TIMEOUT = 10.seconds
    private val READ_TIMEOUT = 30.seconds
    private val WRITE_TIMEOUT = 30.seconds

    @Provides
    @Singleton
    fun providesOkHttp(
        retryInterceptor: RetryInterceptor,
        debugInterceptors: DebugInterceptors
    ) = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT.toJavaDuration())
        .readTimeout(READ_TIMEOUT.toJavaDuration())
        .writeTimeout(WRITE_TIMEOUT.toJavaDuration())
        .addInterceptor(retryInterceptor)
        .apply {
            debugInterceptors().forEach {
                addInterceptor(it)
            }
        }
        .build()

    @Provides
    @Singleton
    @LegacyClient
    fun provideLegacyApolloClient(
        environmentManager: EnvironmentManager,
        okHttpClient: OkHttpClient,
        networkStatusInterceptor: NetworkStatusInterceptor
    ) = ApolloClient.Builder()
        .serverUrl(environmentManager.legacyUrl())
        .okHttpClient(okHttpClient)
        .addHttpInterceptor(LoggingInterceptor(level = BODY))
        .addInterceptor(networkStatusInterceptor)
        .build()

    @Provides
    @Singleton
    @NewClient
    fun provideNewApolloClient(
        environmentManager: EnvironmentManager,
        okHttpClient: OkHttpClient,
        networkStatusInterceptor: NetworkStatusInterceptor
    ) = ApolloClient.Builder()
        .serverUrl(environmentManager.newUrl())
        .okHttpClient(okHttpClient)
        .addHttpInterceptor(LoggingInterceptor(level = BODY))
        .addInterceptor(networkStatusInterceptor)
        .build()

    private fun EnvironmentManager.legacyUrl() = runBlocking { current().legacyGraphQLUrl }
    private fun EnvironmentManager.newUrl() = runBlocking { current().graphQLUrl }
}
