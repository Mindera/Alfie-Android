package au.com.alfie.ecomm.debug.operational.di

import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataEmitter
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataGetter
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalDebugOpModule {

    @Binds
    fun provideAnalyticsDataEmitter(analyticsLogDataHolder: AnalyticsLogDataHolder): AnalyticsLogDataEmitter

    @Binds
    fun provideAnalyticsDataGetter(analyticsLogDataHolder: AnalyticsLogDataHolder): AnalyticsLogDataGetter
}
