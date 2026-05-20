package com.mindera.alfie.debug.operational.view.analytics.model

import com.mindera.alfie.debug.operational.analytics.data.AnalyticsLogData

internal data class AnalyticsLogUI(
    val trackers: Set<String>,
    val events: List<AnalyticsLogData>
)
