package com.mindera.alfie.feature.plp.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.plp.ProductListDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface ProductListModule {

    @Binds
    @IntoSet
    fun bindProductListDeeplinks(productListDeeplinks: ProductListDeeplinks): DeeplinkGroup
}
