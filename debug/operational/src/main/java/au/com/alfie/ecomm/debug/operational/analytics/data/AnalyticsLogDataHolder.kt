package au.com.alfie.ecomm.debug.operational.analytics.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnalyticsLogDataHolder @Inject constructor() : AnalyticsLogDataEmitter, AnalyticsLogDataGetter {

    private val data: MutableList<AnalyticsLogData> = mutableListOf()

    override fun emit(analyticsLogData: AnalyticsLogData) {
        data.add(analyticsLogData)
    }

    override fun get(): List<AnalyticsLogData> = data
}
