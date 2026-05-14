package com.mindera.alfie.domain.usecase.navigation

import com.mindera.alfie.repository.navigation.NavigationRepository
import com.mindera.alfie.repository.navigation.model.NavEntry
import javax.inject.Inject

class GetNavEntriesByParentIdUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {

    suspend operator fun invoke(parentId: Int): List<NavEntry> = navigationRepository.getByParentId(parentId = parentId)
}
