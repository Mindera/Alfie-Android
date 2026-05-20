package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.wishlist.WishlistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val productRepository: ProductRepository,
    private val dispatcherProvider: DispatcherProvider
) : UseCaseInteractor {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UseCaseResult<List<Product>>> =
        wishlistRepository
            .getWishlist()
            .mapLatest { ids ->
                coroutineScope {
                    val results = ids
                        .map { id -> async(dispatcherProvider.io()) { productRepository.getProduct(id) } }
                        .awaitAll()

                    UseCaseResult.Success(
                        results.filterIsInstance<RepositoryResult.Success<Product>>().map { it.data }
                    )
                }
            }
}