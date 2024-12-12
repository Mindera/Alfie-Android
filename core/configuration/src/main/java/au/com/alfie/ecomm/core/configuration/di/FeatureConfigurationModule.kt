package au.com.alfie.ecomm.core.configuration.di

import au.com.alfie.ecomm.core.configuration.handler.FeatureConfigurationManager
import au.com.alfie.ecomm.core.configuration.handler.FeatureHandler
import au.com.alfie.ecomm.core.configuration.handler.RemoteConfigurationHandler
import au.com.alfie.ecomm.core.configuration.handler.RemoteConfigurationManager
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
