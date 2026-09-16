package com.mindera.alfie.repository.bag

import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface BagRepository {

    fun getBag(): Flow<RepositoryResult<List<BagProduct>>>

    fun addToBag(bagProduct: BagProduct): RepositoryResult<Boolean>

    /** Drops a single unit of [bagProduct] — quantity is modelled as repeated entries. */
    fun removeFromBag(bagProduct: BagProduct): RepositoryResult<Boolean>

    /**
     * Drops every unit of [bagProduct] in one write. The Bag screen groups repeated entries into a
     * single line, so removing that line must clear the whole quantity; doing it with N
     * [removeFromBag] calls would emit N times and re-trigger the screen's per-product fetch fan-out
     * on each one.
     */
    fun removeAllFromBag(bagProduct: BagProduct): RepositoryResult<Boolean>
}
