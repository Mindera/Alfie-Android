package au.com.alfie.ecomm.data.wishlist.di

import au.com.alfie.ecomm.data.wishlist.WishlistRepositoryImpl
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
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