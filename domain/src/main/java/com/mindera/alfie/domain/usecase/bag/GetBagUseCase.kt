package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    operator fun invoke(): Flow<UseCaseResult<Bag>> =
        bagRepository.getBag().map { repositoryResult ->
            run(repositoryResult)
        }
}
