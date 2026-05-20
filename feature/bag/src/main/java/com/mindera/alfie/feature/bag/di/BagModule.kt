package com.mindera.alfie.feature.bag.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.bag.BagDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface BagModule {

    @Binds
    @IntoSet
    fun bindBagDeeplinks(bagDeeplinks: BagDeeplinks): DeeplinkGroup
}
