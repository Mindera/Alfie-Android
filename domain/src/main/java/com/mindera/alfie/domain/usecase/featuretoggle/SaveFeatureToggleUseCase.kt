package com.mindera.alfie.domain.usecase.featuretoggle

import com.mindera.alfie.repository.featuretoggle.FeatureToggleRepository
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
import javax.inject.Inject

class SaveFeatureToggleUseCase @Inject constructor(
    private val repository: FeatureToggleRepository
) {
    suspend operator fun invoke(featureToggles: List<FeatureToggle>) =
        repository.saveFeatureToggle(featureToggles)
}
