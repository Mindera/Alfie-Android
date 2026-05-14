package com.mindera.alfie.core.analytics.events

import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.analytics.params.AnalyticsParams

abstract class TrackEvent(
    private val screenName: String,
    private val eventName: String,
    private val analyticsManager: AnalyticsManager
) {

    fun track(params: AnalyticsParams) {
        analyticsManager.trackEvent(
            screenName = screenName,
            eventName = eventName,
            params = params
        )
    }
}
