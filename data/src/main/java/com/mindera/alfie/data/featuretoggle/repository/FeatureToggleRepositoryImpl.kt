package com.mindera.alfie.data.featuretoggle.repository

import com.mindera.alfie.data.database.search.FeatureToggleDao
import com.mindera.alfie.data.featuretoggle.mapper.toDB
import com.mindera.alfie.data.featuretoggle.mapper.toDomain
import com.mindera.alfie.repository.featuretoggle.FeatureToggleRepository
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
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
