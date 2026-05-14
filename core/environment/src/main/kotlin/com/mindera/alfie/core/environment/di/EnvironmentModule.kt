package com.mindera.alfie.core.environment.di

import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.EnvironmentManagerImpl
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
