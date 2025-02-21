package au.com.alfie.ecomm.repository.bag

import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult

interface BagRepository {

    fun addToBag(product: Product): RepositoryResult<Boolean>

    fun getBag(): RepositoryResult<List<Product>>
}
