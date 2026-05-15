package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
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