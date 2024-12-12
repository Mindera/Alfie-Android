package au.com.alfie.ecomm.domain.usecase.navigation

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.navigation.NavigationRepository
import au.com.alfie.ecomm.repository.navigation.model.HandleType.HEADER
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import javax.inject.Inject

class GetRootNavEntriesUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<NavEntry>> =
        run(navigationRepository.getNavEntriesByHandle(handleType = HEADER))
}
