package au.com.alfie.ecomm.repository.bag

import au.com.alfie.ecomm.repository.result.RepositoryResult

interface BagRepository {

    fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean>

    fun getBag(): RepositoryResult<List<BagProduct>>
}
