package au.com.alfie.ecomm.repository.featuretoggle

import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import kotlinx.coroutines.flow.Flow

interface FeatureToggleRepository {
    fun getAllFeatureTogglesAsFlow(): Flow<List<FeatureToggle>>
    suspend fun saveFeatureToggle(featureToggles: List<FeatureToggle>)
    suspend fun updateFeatureToggle(featureToggle: FeatureToggle)
    fun getFeatureTogglesByNameAsFlow(toggleTitle: String): Flow<List<FeatureToggle>>
}
