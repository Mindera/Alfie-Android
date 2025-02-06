package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import javax.inject.Inject

class AddWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String) =
        run(productRepository.getProduct(productId = productId))
            .doOnResult(
                onSuccess = {
                    wishlistRepository.addToWishlist(it)
                },
                onError = {
                    it
                }
            )
}
