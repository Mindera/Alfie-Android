package au.com.alfie.ecomm.core.analytics.events

import au.com.alfie.ecomm.core.analytics.AnalyticsManager
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams

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
