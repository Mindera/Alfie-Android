package au.com.alfie.ecomm.core.analytics.providers

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.events.EventKey.ERROR_MESSAGE
import au.com.alfie.ecomm.core.analytics.events.EventKey.SCREEN_NAME
import au.com.alfie.ecomm.core.analytics.events.toFirebaseEventErrorValue
import au.com.alfie.ecomm.core.analytics.events.toFirebaseEventKey
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams
import au.com.alfie.ecomm.core.analytics.params.AnalyticsValues.StringValues
import au.com.alfie.ecomm.core.analytics.params.toBundle
import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

internal class FirebaseAnalyticsProvider @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val analyticsLogger: AnalyticsLogger
) : AnalyticsProvider {

    override val trackerName: String = "Firebase"

    override fun trackEvent(
        screenName: String,
        eventName: String,
        params: AnalyticsParams
    ) {
        analyticsLogger.trackAndLog(
            event = eventName,
            params = params,
            extraParams = {
                mapOf(SCREEN_NAME.toFirebaseEventKey() to StringValues(screenName))
            },
            map = { toBundle() }
        ) { bundle ->
            runCatching { firebaseAnalytics.logEvent(eventName, bundle) }
        }
    }

    override fun trackError(
        screenName: String,
        eventName: String,
        eventErrorValue: EventErrorValue,
        params: AnalyticsParams
    ) {
        analyticsLogger.trackAndLog(
            event = eventName,
            params = params,
            extraParams = {
                mapOf(
                    SCREEN_NAME.toFirebaseEventKey() to StringValues(screenName),
                    ERROR_MESSAGE.toFirebaseEventKey() to StringValues(eventErrorValue.toFirebaseEventErrorValue())
                )
            },
            map = { toBundle() }
        ) { bundle ->
            runCatching { firebaseAnalytics.logEvent(eventName, bundle) }
        }
    }
}
