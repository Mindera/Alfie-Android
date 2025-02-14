package au.com.alfie.ecomm.data.bag

import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.result.RepositoryResult
import javax.inject.Inject

class BagRepositoryImpl @Inject constructor() : BagRepository {

    // TODO consider removing this property when the products in the bag are saved on database or api
    private val bag = mutableListOf<BagProduct>()

    // TODO change this implementation to a proper implementation using data base or api to save the product
    override fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean> {
        bag.firstOrNull {
            bagProduct.productId == it.productId && bagProduct.variantSku == it.variantSku
        } ?: bag.add(bagProduct)
        return RepositoryResult.Success(true)
    }

    // TODO change this implementation to a proper implementation using data base or api to get the products in the bag
    override fun getBag(): RepositoryResult<List<BagProduct>> {
        return Result.success(bag).toRepositoryResult()
    }
}
