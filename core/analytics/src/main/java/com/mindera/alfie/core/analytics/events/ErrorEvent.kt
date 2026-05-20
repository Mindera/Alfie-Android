package com.mindera.alfie.core.analytics.events

import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.analytics.params.AnalyticsParams

abstract class ErrorEvent(
    private val screenName: String,
    private val eventName: String,
    private val analyticsManager: AnalyticsManager
) {

    fun track(
        eventErrorValue: EventErrorValue,
        params: AnalyticsParams
    ) {
        analyticsManager.trackError(
            screenName = screenName,
            eventName = eventName,
            eventErrorValue = eventErrorValue,
            params = params
        )
    }
}
