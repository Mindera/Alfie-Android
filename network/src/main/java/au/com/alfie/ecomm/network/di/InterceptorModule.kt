package au.com.alfie.ecomm.network.di

import au.com.alfie.ecomm.network.interceptor.NetworkStatusInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface InterceptorModule {

    @Singleton
    @Binds
    fun provideNetworkStatusInterceptor(networkStatusInterceptor: NetworkStatusInterceptor): ApolloInterceptor
}
