package au.com.alfie.ecomm.core.configuration.di

import android.content.Context
import au.com.alfie.ecomm.core.configuration.R
import au.com.alfie.ecomm.core.configuration.provider.LOCAL_FEATURE_CONFIGURATION
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FeatureProviderModule {

    @Provides
    @Singleton
    @Named(LOCAL_FEATURE_CONFIGURATION)
    fun provideLocalFeatureConfiguration(@ApplicationContext context: Context): String =
        context.resources.openRawResource(R.raw.local_feature_configuration).bufferedReader().use { it.readText() }
}
