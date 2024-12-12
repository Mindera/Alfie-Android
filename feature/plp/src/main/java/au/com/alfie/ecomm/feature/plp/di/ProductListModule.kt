package au.com.alfie.ecomm.feature.plp.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.plp.ProductListDeeplinks
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
