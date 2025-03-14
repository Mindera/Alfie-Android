package au.com.alfie.ecomm.data.bag

import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BagRepositoryImpl @Inject constructor() : BagRepository {

    // TODO consider removing this property when the products in the bag are saved on database or api
    private val _bag = MutableStateFlow<List<BagProduct>>(listOf())

    // TODO change this implementation to a proper implementation using data base or api to get the products in the bag
    override fun getBag(): Flow<RepositoryResult<List<BagProduct>>> {
        return _bag.map { bag ->
            Result.success(bag).toRepositoryResult()
        }
    }

    // TODO change this implementation to a proper implementation using data base or api to save the product
    override fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean> {
        _bag.value = _bag.value.toMutableList().apply { add(bagProduct) }
        return RepositoryResult.Success(true)
    }

    override fun removeFromBag(bagProduct: BagProduct): RepositoryResult<Boolean> {
        _bag.value = _bag.value.toMutableList().apply { remove(bagProduct) }
        return RepositoryResult.Success(true)
    }
}
