package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.repository.wishlist.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String) =
        run(wishlistRepository.removeFromWishlist(productId))
}