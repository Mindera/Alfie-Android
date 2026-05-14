package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : UseCaseInteractor {

    operator fun invoke(): Flow<UseCaseResult<List<Product>>> =
        wishlistRepository.getWishlist().map { repositoryResult ->
            run(repositoryResult)
        }
}