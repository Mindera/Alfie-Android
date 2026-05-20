package com.mindera.alfie.data.wishlist

import com.mindera.alfie.data.database.wishlist.WishlistDao
import com.mindera.alfie.data.database.wishlist.model.WishlistEntity
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getWishlist(): Flow<List<String>> =
        wishlistDao.getWishlistIds().map { wishlist ->
            wishlist.map { it.id }
        }

    override suspend fun addToWishlist(productId: String) =
        runCatching { wishlistDao.addToWishlist(WishlistEntity(productId)) }
            .toRepositoryResult()

    override suspend fun removeFromWishlist(productId: String) =
        runCatching { wishlistDao.removeFromWishlist(productId) }
            .toRepositoryResult()
}
