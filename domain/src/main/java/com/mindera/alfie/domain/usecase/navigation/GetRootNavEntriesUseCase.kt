package com.mindera.alfie.domain.usecase.navigation

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.navigation.NavigationRepository
import com.mindera.alfie.repository.navigation.model.HandleType.HEADER
import com.mindera.alfie.repository.navigation.model.NavEntry
import javax.inject.Inject

class GetRootNavEntriesUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<NavEntry>> =
        run(navigationRepository.getNavEntriesByHandle(handleType = HEADER))
}
