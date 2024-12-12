package au.com.alfie.ecomm.data.featuretoggle.repository

import au.com.alfie.ecomm.data.database.search.FeatureToggleDao
import au.com.alfie.ecomm.data.featuretoggle.mapper.toDomain
import au.com.alfie.ecomm.data.featuretoggle.mapper.toDB
import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeatureToggleRepositoryImpl @Inject constructor(
    private val featureToggleDao: FeatureToggleDao
) : FeatureToggleRepository {
    override fun getAllFeatureTogglesAsFlow(): Flow<List<FeatureToggle>> =
        featureToggleDao.getAllFeatureTogglesAsFlow().map { it.toDomain() }

    override suspend fun saveFeatureToggle(featureToggles: List<FeatureToggle>) =
        featureToggleDao.insertFeatureToggle(featureToggles.toDB())

    override suspend fun updateFeatureToggle(featureToggle: FeatureToggle) =
        featureToggleDao.updateFeatureToggle(
            featureToggle.toDB().toggleTitle,
            featureToggle.toDB().enabled
        )

    override fun getFeatureTogglesByNameAsFlow(toggleTitle: String) =
        featureToggleDao.getFeatureTogglesByNameAsFlow(toggleTitle).map { it.toDomain() }
}
