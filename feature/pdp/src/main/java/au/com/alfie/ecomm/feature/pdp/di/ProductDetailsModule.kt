package au.com.alfie.ecomm.feature.pdp.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.pdp.ProductDetailsDeeplinks
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
