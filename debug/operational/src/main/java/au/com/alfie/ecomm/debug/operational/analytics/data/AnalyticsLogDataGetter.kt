package au.com.alfie.ecomm.debug.operational.analytics.data

internal interface AnalyticsLogDataGetter {

    fun get(): List<AnalyticsLogData>
}
