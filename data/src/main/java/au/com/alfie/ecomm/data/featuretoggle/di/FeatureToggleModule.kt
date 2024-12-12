package au.com.alfie.ecomm.data.featuretoggle.di

import au.com.alfie.ecomm.data.featuretoggle.repository.FeatureToggleRepositoryImpl
import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
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
