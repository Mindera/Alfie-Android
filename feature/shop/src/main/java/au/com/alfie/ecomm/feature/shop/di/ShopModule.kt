package au.com.alfie.ecomm.feature.shop.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.shop.ShopDeeplinks
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
