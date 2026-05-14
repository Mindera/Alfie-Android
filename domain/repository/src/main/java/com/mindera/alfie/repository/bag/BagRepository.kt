package com.mindera.alfie.repository.bag

import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface BagRepository {

    fun getBag(): Flow<RepositoryResult<List<BagProduct>>>

    fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean>

    fun removeFromBag(bagProduct: BagProduct): RepositoryResult<Boolean>
}
