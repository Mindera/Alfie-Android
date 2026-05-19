package com.mindera.alfie.repository.wishlist

import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<List<String>>
    suspend fun addToWishlist(productId: String): RepositoryResult<Unit>
    suspend fun removeFromWishlist(productId: String): RepositoryResult<Unit>
}
