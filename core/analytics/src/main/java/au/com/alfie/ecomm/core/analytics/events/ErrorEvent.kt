package au.com.alfie.ecomm.core.analytics.events

import au.com.alfie.ecomm.core.analytics.AnalyticsManager
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams

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
