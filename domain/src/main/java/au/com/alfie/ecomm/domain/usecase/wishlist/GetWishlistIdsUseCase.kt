package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistIdsUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : UseCaseInteractor {

    operator fun invoke(): Flow<List<String>> = wishlistRepository.getWishlist()
}