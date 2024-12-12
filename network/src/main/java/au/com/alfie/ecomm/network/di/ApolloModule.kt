package au.com.alfie.ecomm.network.di

import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.debug.interceptor.DebugInterceptors
import au.com.alfie.ecomm.network.interceptor.NetworkStatusInterceptor
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.http.LoggingInterceptor.Level.BODY
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApolloModule {

    @Provides
    @Singleton
    fun providesOkHttp(
        debugInterceptors: DebugInterceptors
    ) = OkHttpClient.Builder()
        .apply {
            debugInterceptors().forEach {
                addInterceptor(it)
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideApolloClient(
        environmentManager: EnvironmentManager,
        okHttpClient: OkHttpClient,
        networkStatusInterceptor: NetworkStatusInterceptor
    ) = ApolloClient.Builder()
        .serverUrl(environmentManager.url())
        .okHttpClient(okHttpClient)
        .addHttpInterceptor(LoggingInterceptor(level = BODY))
        .addInterceptor(networkStatusInterceptor)
        .build()

    private fun EnvironmentManager.url() = runBlocking { current().graphQLUrl }
}
