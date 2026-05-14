package com.mindera.alfie.repository.wishlist

import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    fun addToWishlist(product: Product): RepositoryResult<Boolean>

    fun removeFromWishlist(product: Product): RepositoryResult<Boolean>

    fun getWishlist(): Flow<RepositoryResult<List<Product>>>
}
