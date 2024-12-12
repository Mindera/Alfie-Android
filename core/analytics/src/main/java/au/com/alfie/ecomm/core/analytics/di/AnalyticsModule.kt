package au.com.alfie.ecomm.core.analytics.di

import au.com.alfie.ecomm.core.analytics.AnalyticsManager
import au.com.alfie.ecomm.core.analytics.AnalyticsManagerImpl
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAnalytics() = Firebase.analytics
    }

    @Binds
    abstract fun bindAnalyticsManager(analyticsManagerImpl: AnalyticsManagerImpl): AnalyticsManager
}
