package au.com.alfie.ecomm.feature.home.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.home.HomeDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface HomeModule {

    @Binds
    @IntoSet
    fun bindHomeDeeplinks(homeDeeplinks: HomeDeeplinks): DeeplinkGroup
}
