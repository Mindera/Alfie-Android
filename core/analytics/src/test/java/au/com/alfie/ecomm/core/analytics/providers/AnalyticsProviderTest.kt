package au.com.alfie.ecomm.core.analytics.providers

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams
import au.com.alfie.ecomm.core.analytics.params.AnalyticsValues
import au.com.alfie.ecomm.core.analytics.params.FakeAnalyticsParams
import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class AnalyticsProviderTest {

    @RelaxedMockK
    lateinit var analyticsLogger: AnalyticsLogger

    private val subject = FakeAnalyticsProvider()

    @Test
    fun `trackAndLog - when the track result is success then should call the logger`() {
        with(subject) {
            analyticsLogger.trackAndLog(
                event = "event",
                params = FakeAnalyticsParams(
                    mapOf("param1" to AnalyticsValues.IntValues(123))
                ),
                extraParams = {
                    mapOf(
                        "extra1" to AnalyticsValues.StringValues("value1"),
                        "extra2" to AnalyticsValues.StringValues("value2")
                    )
                },
                map = {
                    params().values.toList()
                },
                track = {
                    val expected = listOf(
                        AnalyticsValues.IntValues(123),
                        AnalyticsValues.StringValues("value1"),
                        AnalyticsValues.StringValues("value2")
                    )

                    assertEquals(expected, it)
                    Result.success(Unit)
                }
            )
        }

        verify {
            analyticsLogger.logEvent(
                tracker = "fakeTracker",
                event = "event",
                params = mapOf(
                    "param1" to "123",
                    "extra1" to "value1",
                    "extra2" to "value2"
                ),
                timestamp = any()
            )
        }
    }

    @Test
    fun `trackAndLog - when the track result is fail then should not call the logger`() {
        with(subject) {
            analyticsLogger.trackAndLog(
                event = "event",
                params = FakeAnalyticsParams(),
                extraParams = { mapOf() },
                map = {
                    params().values.toList()
                },
                track = {
                    Result.failure<Unit>(IllegalArgumentException())
                }
            )
        }

        verify(exactly = 0) {
            analyticsLogger.logEvent(
                tracker = any(),
                event = any(),
                params = any(),
                timestamp = any()
            )
        }
    }

    inner class FakeAnalyticsProvider : AnalyticsProvider {

        override val trackerName: String = "fakeTracker"

        override fun trackEvent(screenName: String, eventName: String, params: AnalyticsParams) = Unit

        override fun trackError(screenName: String, eventName: String, eventErrorValue: EventErrorValue, params: AnalyticsParams) = Unit
    }
}
