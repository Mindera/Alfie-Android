package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): Flow<UseCaseResult<List<BagProduct>>> =
        bagRepository.getBag().map { repositoryResult ->
            run(repositoryResult)
        }
}