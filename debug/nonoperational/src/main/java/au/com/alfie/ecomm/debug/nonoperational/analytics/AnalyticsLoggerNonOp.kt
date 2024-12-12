package au.com.alfie.ecomm.debug.nonoperational.analytics

import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import javax.inject.Inject

internal class AnalyticsLoggerNonOp @Inject constructor() : AnalyticsLogger {

    override fun logEvent(
        tracker: String,
        event: String,
        params: Map<String, String>,
        timestamp: Long
    ) = Unit
}
