package au.com.alfie.ecomm.repository.wishlist

import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WishlistRepository {

    fun addToWishlist(product: Product): RepositoryResult<Boolean>

    fun removeFromWishlist(product: Product): RepositoryResult<Unit>

    fun getWishlist(): Flow<RepositoryResult<List<Product>>>
}
