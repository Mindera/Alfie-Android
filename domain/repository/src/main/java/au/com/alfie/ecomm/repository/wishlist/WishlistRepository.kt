package au.com.alfie.ecomm.repository.wishlist

import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    fun addToWishlist(product: Product): RepositoryResult<Boolean>

    fun removeFromWishlist(product: Product): RepositoryResult<Boolean>

    fun getWishlist(): Flow<RepositoryResult<List<Product>>>
}
