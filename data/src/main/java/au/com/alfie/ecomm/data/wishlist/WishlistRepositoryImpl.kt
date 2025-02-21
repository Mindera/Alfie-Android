package au.com.alfie.ecomm.data.wishlist

import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor() : WishlistRepository {

    // TODO consider removing this property when the wishlist of product is saved on database or api
    private val _wishlist = MutableStateFlow<List<Product>>(listOf())

    // TODO change this implementation to a proper implementation using data base or api to save the products on wishlist
    override fun addToWishlist(product: Product): RepositoryResult<Boolean> {
        if (_wishlist.value.none { it.id == product.id }) {
            _wishlist.value = buildList {
                addAll(_wishlist.value)
                add(product)
            }
        }
        return RepositoryResult.Success(true)
    }

    // TODO change this implementation to a proper implementation using data base or api to save the products on wishlist
    override fun removeFromWishlist(product: Product): RepositoryResult<Boolean> {
        _wishlist.value = _wishlist.value.filter { it.id != product.id }.toMutableList()
        return RepositoryResult.Success(true)
    }

    // TODO change this implementation to a proper implementation using data base or api to get the wishlist
    override fun getWishlist(): Flow<RepositoryResult<List<Product>>> {
        return _wishlist.map { list ->
            Result.success(list).toRepositoryResult()
        }
    }
}
