package com.mindera.alfie.debug.nonoperational.analytics

import com.mindera.alfie.debug.analytics.AnalyticsLogger
import javax.inject.Inject

internal class AnalyticsLoggerNonOp @Inject constructor() : AnalyticsLogger {

    override fun logEvent(
        tracker: String,
        event: String,
        params: Map<String, String>,
        timestamp: Long
    ) = Unit
}
