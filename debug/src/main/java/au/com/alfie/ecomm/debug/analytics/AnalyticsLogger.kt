package au.com.alfie.ecomm.debug.analytics

interface AnalyticsLogger {

    fun logEvent(
        tracker: String,
        event: String,
        params: Map<String, String>,
        timestamp: Long
    )
}
