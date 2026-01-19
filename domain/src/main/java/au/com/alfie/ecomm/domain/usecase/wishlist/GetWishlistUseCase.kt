package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.onSuccess
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Product>> =
        wishlistRepository
            .getWishlist()
            .mapLatest { ids ->
                coroutineScope {
                    val deferredProducts: List<Deferred<Product?>> = ids.map { id ->
                        async(Dispatchers.IO) {
                            var product: Product? = null
                            productRepository.getProduct(id)
                                .onSuccess { product = it }
                            product
                        }
                    }
                    deferredProducts.awaitAll().filterNotNull()
                }
            }

}