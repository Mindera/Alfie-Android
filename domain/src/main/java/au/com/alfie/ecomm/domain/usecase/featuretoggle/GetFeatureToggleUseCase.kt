package au.com.alfie.ecomm.domain.usecase.featuretoggle

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeatureToggleUseCase @Inject constructor(
    private val featureToggleRepository: FeatureToggleRepository
) : UseCaseInteractor {

    operator fun invoke(toggleTitle: String): Flow<List<FeatureToggle>> =
        featureToggleRepository.getFeatureTogglesByNameAsFlow(toggleTitle)
}
