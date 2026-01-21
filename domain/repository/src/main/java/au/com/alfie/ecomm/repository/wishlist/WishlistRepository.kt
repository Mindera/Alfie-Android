package au.com.alfie.ecomm.repository.wishlist

import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<List<String>>
    suspend fun addToWishlist(productId: String): RepositoryResult<Unit>
    suspend fun removeFromWishlist(productId: String): RepositoryResult<Unit>
}
