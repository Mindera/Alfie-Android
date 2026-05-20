package com.mindera.alfie.data.featuretoggle.mapper

import com.mindera.alfie.data.database.search.model.FeatureToggleEntity
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle

internal fun FeatureToggleEntity.toDomain() = FeatureToggle(
    toggleTitle = toggleTitle,
    enabled = enabled,
    type = type
)

internal fun FeatureToggle.toDB() = FeatureToggleEntity(
    toggleTitle = toggleTitle,
    enabled = enabled,
    type = type
)

internal fun List<FeatureToggle>.toDB(): List<FeatureToggleEntity> = map {
    it.toDB()
}

internal fun List<FeatureToggleEntity>.toDomain(): List<FeatureToggle> = map {
    it.toDomain()
}
