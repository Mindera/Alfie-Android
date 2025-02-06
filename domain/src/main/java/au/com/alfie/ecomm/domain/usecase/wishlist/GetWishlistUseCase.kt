package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : UseCaseInteractor {

    operator fun invoke(): Flow<UseCaseResult<List<Product>>> {
        return wishlistRepository.getWishlist().map { repositoryResult ->
            run(repositoryResult)
        }
    }
}