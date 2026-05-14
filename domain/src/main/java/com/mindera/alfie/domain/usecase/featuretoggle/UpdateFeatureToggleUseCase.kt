package com.mindera.alfie.domain.usecase.featuretoggle

import com.mindera.alfie.repository.featuretoggle.FeatureToggleRepository
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
import javax.inject.Inject

class UpdateFeatureToggleUseCase @Inject constructor(
    private val repository: FeatureToggleRepository
) {
    suspend operator fun invoke(featureToggle: FeatureToggle) =
        repository.updateFeatureToggle(featureToggle)
}
