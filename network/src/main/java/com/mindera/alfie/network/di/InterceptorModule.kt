package com.mindera.alfie.network.di

import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.mindera.alfie.network.interceptor.NetworkStatusInterceptor
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
