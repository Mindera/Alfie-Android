package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String) =
        run(wishlistRepository.addToWishlist(productId))
}
