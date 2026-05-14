package com.mindera.alfie.feature.shop.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.shop.ShopDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface ShopModule {

    @Binds
    @IntoSet
    fun bindShopDeeplinks(shopDeeplinks: ShopDeeplinks): DeeplinkGroup
}
