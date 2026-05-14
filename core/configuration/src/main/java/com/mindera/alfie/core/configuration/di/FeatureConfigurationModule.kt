package com.mindera.alfie.core.configuration.di

import com.mindera.alfie.core.configuration.handler.FeatureConfigurationManager
import com.mindera.alfie.core.configuration.handler.FeatureHandler
import com.mindera.alfie.core.configuration.handler.RemoteConfigurationHandler
import com.mindera.alfie.core.configuration.handler.RemoteConfigurationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FeatureConfigurationModule {

    @Binds
    fun provideFeatureConfigurationManager(configurationManager: FeatureConfigurationManager): FeatureHandler

    @Binds
    fun provideRemoteConfigurationManager(configurationManager: RemoteConfigurationManager): RemoteConfigurationHandler
}
