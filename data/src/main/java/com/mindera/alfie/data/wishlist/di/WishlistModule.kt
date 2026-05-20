package com.mindera.alfie.data.wishlist.di

import com.mindera.alfie.data.wishlist.WishlistRepositoryImpl
import com.mindera.alfie.repository.wishlist.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WishlistModule {

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(wishlistRepositoryImpl: WishlistRepositoryImpl): WishlistRepository
}