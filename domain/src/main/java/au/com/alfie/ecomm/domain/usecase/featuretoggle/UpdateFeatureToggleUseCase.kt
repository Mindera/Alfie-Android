package au.com.alfie.ecomm.domain.usecase.featuretoggle

import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import javax.inject.Inject

class UpdateFeatureToggleUseCase @Inject constructor(
    private val repository: FeatureToggleRepository
) {
    suspend operator fun invoke(featureToggle: FeatureToggle) =
        repository.updateFeatureToggle(featureToggle)
}
