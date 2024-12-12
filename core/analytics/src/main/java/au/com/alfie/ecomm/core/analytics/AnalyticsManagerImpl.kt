package au.com.alfie.ecomm.core.analytics

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams
import au.com.alfie.ecomm.core.analytics.providers.FirebaseAnalyticsProvider
import javax.inject.Inject

internal class AnalyticsManagerImpl @Inject constructor(
    private val firebaseAnalyticsProvider: FirebaseAnalyticsProvider
) : AnalyticsManager {

    override fun trackEvent(
        screenName: String,
        eventName: String,
        params: AnalyticsParams
    ) {
        firebaseAnalyticsProvider.trackEvent(
            screenName = screenName,
            eventName = eventName,
            params = params
        )
    }

    override fun trackError(
        screenName: String,
        eventName: String,
        eventErrorValue: EventErrorValue,
        params: AnalyticsParams
    ) {
        firebaseAnalyticsProvider.trackError(
            screenName = screenName,
            eventName = eventName,
            eventErrorValue = eventErrorValue,
            params = params
        )
    }
}
