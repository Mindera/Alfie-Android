package com.mindera.alfie.debug.operational.analytics

import com.mindera.alfie.core.commons.util.DateUtils
import com.mindera.alfie.debug.analytics.AnalyticsLogger
import com.mindera.alfie.debug.operational.analytics.data.AnalyticsLogData
import com.mindera.alfie.debug.operational.analytics.data.AnalyticsLogDataEmitter
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "AnalyticsLogger"
private const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"

internal class AnalyticsLoggerOp @Inject constructor(
    private val analyticsLogDataEmitter: AnalyticsLogDataEmitter
) : AnalyticsLogger {

    override fun logEvent(
        tracker: String,
        event: String,
        params: Map<String, String>,
        timestamp: Long
    ) {
        with(
            AnalyticsLogData(
                tracker = tracker,
                event = event,
                params = params,
                timestamp = DateUtils.convertMsToDate(timestamp, DATE_PATTERN)
            )
        ) {
            analyticsLogDataEmitter.emit(this)
            Timber.tag(TAG).i(this.toString())
        }
    }
}
