package com.mindera.alfie.data.featuretoggle.di

import com.mindera.alfie.data.featuretoggle.repository.FeatureToggleRepositoryImpl
import com.mindera.alfie.repository.featuretoggle.FeatureToggleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FeatureToggleModule {

    @Binds
    abstract fun bindFeatureToggleRepository(featureToggleRepositoryImpl: FeatureToggleRepositoryImpl): FeatureToggleRepository
}
