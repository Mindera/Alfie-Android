package com.mindera.alfie.feature.pdp.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.pdp.ProductDetailsDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface ProductDetailsModule {

    @Binds
    @IntoSet
    fun bindProductDetailsDeeplinks(productDetailsDeeplinks: ProductDetailsDeeplinks): DeeplinkGroup
}
