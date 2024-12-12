package au.com.alfie.ecomm.core.analytics

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams

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
