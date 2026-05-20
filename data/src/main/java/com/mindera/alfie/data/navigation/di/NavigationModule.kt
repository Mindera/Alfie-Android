package com.mindera.alfie.data.navigation.di

import com.mindera.alfie.data.navigation.remote.service.RemoteNavigationService
import com.mindera.alfie.data.navigation.remote.service.RemoteNavigationServiceImpl
import com.mindera.alfie.data.navigation.repository.NavigationRepositoryImpl
import com.mindera.alfie.repository.navigation.NavigationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigationModule {

    @Binds
    abstract fun bindRemoteNavigationService(
        remoteNavigationServiceImpl: RemoteNavigationServiceImpl
    ): RemoteNavigationService

    @Binds
    abstract fun bindNavigationRepository(
        navigationRepositoryImpl: NavigationRepositoryImpl
    ): NavigationRepository
}
