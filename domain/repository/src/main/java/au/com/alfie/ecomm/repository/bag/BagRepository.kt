package au.com.alfie.ecomm.repository.bag

import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult

interface BagRepository {

    fun addToBag(product: Product)

    fun getBag(): RepositoryResult<List<Product>>
}
