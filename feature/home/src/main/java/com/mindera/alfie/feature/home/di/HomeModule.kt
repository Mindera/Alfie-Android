package com.mindera.alfie.feature.home.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.home.HomeDeeplinks
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
