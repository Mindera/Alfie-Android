package au.com.alfie.ecomm.core.analytics.providers

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams
import au.com.alfie.ecomm.core.analytics.params.AnalyticsValues
import au.com.alfie.ecomm.core.analytics.params.plus
import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import timber.log.Timber
import java.time.Clock

internal interface AnalyticsProvider {

    val trackerName: String

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

    fun <T> AnalyticsLogger.trackAndLog(
        event: String,
        params: AnalyticsParams,
        extraParams: () -> Map<String, AnalyticsValues>,
        map: AnalyticsParams.() -> T,
        track: (T) -> Result<*>
    ) {
        val paramsJoined = params + extraParams()

        track(map(paramsJoined))
            .onSuccess {
                logEvent(
                    tracker = trackerName,
                    event = event,
                    params = paramsJoined.params().mapValues { it.value.toString() },
                    timestamp = Clock.systemUTC().millis()
                )
            }
            .onFailure { Timber.e(it) }
    }
}
