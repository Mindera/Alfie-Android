package au.com.alfie.ecomm.domain.usecase.featuretoggle

import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import javax.inject.Inject

class SaveFeatureToggleUseCase @Inject constructor(
    private val repository: FeatureToggleRepository
) {
    suspend operator fun invoke(featureToggles: List<FeatureToggle>) =
        repository.saveFeatureToggle(featureToggles)
}
