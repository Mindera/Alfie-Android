package au.com.alfie.ecomm.domain.usecase.navigation

import au.com.alfie.ecomm.repository.navigation.NavigationRepository
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import javax.inject.Inject

class GetNavEntriesByParentIdUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {

    suspend operator fun invoke(parentId: Int): List<NavEntry> = navigationRepository.getByParentId(parentId = parentId)
}
