package au.com.alfie.ecomm.core.environment.di

import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.EnvironmentManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EnvironmentModule {

    @Binds
    fun bindEnvironmentManager(impl: EnvironmentManagerImpl): EnvironmentManager
}
