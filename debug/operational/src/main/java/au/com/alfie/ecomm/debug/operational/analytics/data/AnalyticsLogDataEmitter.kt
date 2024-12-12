package au.com.alfie.ecomm.debug.operational.analytics.data

internal interface AnalyticsLogDataEmitter {

    fun emit(analyticsLogData: AnalyticsLogData)
}
