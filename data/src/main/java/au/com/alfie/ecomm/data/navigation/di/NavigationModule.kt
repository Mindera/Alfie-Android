package au.com.alfie.ecomm.data.navigation.di

import au.com.alfie.ecomm.data.navigation.remote.service.RemoteNavigationService
import au.com.alfie.ecomm.data.navigation.remote.service.RemoteNavigationServiceImpl
import au.com.alfie.ecomm.data.navigation.repository.NavigationRepositoryImpl
import au.com.alfie.ecomm.repository.navigation.NavigationRepository
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
