package au.com.alfie.ecomm.feature.wishlist.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.wishlist.WishlistDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface WishlistModule {

    @Binds
    @IntoSet
    fun bindWishlistDeeplinks(wishlistDeeplinks: WishlistDeeplinks): DeeplinkGroup
}
