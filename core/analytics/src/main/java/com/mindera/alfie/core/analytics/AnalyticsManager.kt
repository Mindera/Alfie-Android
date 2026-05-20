package com.mindera.alfie.core.analytics

import com.mindera.alfie.core.analytics.events.EventErrorValue
import com.mindera.alfie.core.analytics.params.AnalyticsParams

interface AnalyticsManager {

    fun trackEvent(
        screenName: String,
        eventName: String,
        params: AnalyticsParams
    )

    fun trackError(
        screenName: String,
        eventName: String,
        eventErrorValue: EventErrorValue,
        params: AnalyticsParams
    )
}
