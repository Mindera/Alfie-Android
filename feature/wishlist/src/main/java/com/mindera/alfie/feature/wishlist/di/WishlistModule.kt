package com.mindera.alfie.feature.wishlist.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.wishlist.WishlistDeeplinks
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
