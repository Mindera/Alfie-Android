package au.com.alfie.ecomm.data.wishlist

import au.com.alfie.ecomm.data.database.wishlist.WishlistDao
import au.com.alfie.ecomm.data.database.wishlist.model.WishlistEntity
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getWishlist(): Flow<List<String>> =
        wishlistDao.getWishlistIds().map { wishlist ->
            Timber.tag("WishlistTesting").d("Fetching wishlist: $wishlist")
            wishlist.map { it.id }
        }

    override suspend fun addToWishlist(productId: String) =
        runCatching {
            Timber.tag("WishlistTesting").d("Adding to wishlist: $productId")
            wishlistDao.addToWishlist(WishlistEntity(productId))
        }
            .toRepositoryResult()

    override suspend fun removeFromWishlist(productId: String) =
        runCatching {
            Timber.tag("WishlistTesting").d("Removing from wishlist: $productId")
            wishlistDao.removeFromWishlist(productId)
        }
            .toRepositoryResult()
}
