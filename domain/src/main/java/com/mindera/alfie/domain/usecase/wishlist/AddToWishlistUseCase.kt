package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.wishlist.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String) =
        productRepository.getProduct(productId = productId)
            .doOnResult(
                onSuccess = { run(wishlistRepository.addToWishlist(it)) },
                onError = { UseCaseResult.Error(it) }
            )
}
