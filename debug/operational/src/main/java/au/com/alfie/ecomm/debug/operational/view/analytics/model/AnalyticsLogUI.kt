package au.com.alfie.ecomm.debug.operational.view.analytics.model

import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogData

internal data class AnalyticsLogUI(
    val trackers: Set<String>,
    val events: List<AnalyticsLogData>
)
