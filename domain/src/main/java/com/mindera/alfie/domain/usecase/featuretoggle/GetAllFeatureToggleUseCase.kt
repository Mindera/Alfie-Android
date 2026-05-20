package com.mindera.alfie.domain.usecase.featuretoggle

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.repository.featuretoggle.FeatureToggleRepository
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFeatureToggleUseCase @Inject constructor(
    private val featureToggleRepository: FeatureToggleRepository
) : UseCaseInteractor {

    operator fun invoke(): Flow<List<FeatureToggle>> = featureToggleRepository.getAllFeatureTogglesAsFlow()
}
