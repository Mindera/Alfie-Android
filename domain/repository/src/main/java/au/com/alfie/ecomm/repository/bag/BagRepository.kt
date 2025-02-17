package au.com.alfie.ecomm.repository.bag

import au.com.alfie.ecomm.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface BagRepository {

    fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean>

    fun getBag(): Flow<RepositoryResult<List<BagProduct>>>

    fun removeFromBag(bagProduct: BagProduct): RepositoryResult<Boolean>
}
