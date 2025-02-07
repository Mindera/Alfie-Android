package au.com.alfie.ecomm.data.bag

import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import javax.inject.Inject

class BagRepositoryImpl @Inject constructor() : BagRepository {

    // TODO consider removing this property when the products in the bag are saved on database or api
    private val bag = mutableListOf<Product>()

    // TODO change this implementation to a proper implementation using data base or api to save the product
    override fun addToBag(product: Product): RepositoryResult<Boolean> {
        bag.firstOrNull { product.id == it.id } ?: bag.add(product)
        return RepositoryResult.Success(true)
    }

    // TODO change this implementation to a proper implementation using data base or api to get the products in the bag
    override fun getBag(): RepositoryResult<List<Product>> {
        return Result.success(bag).toRepositoryResult()
    }
}
