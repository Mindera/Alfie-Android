package au.com.alfie.ecomm.core.analytics.providers

import android.os.Bundle
import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.events.EventKey.ERROR_MESSAGE
import au.com.alfie.ecomm.core.analytics.events.EventKey.SCREEN_NAME
import au.com.alfie.ecomm.core.analytics.events.toFirebaseEventErrorValue
import au.com.alfie.ecomm.core.analytics.events.toFirebaseEventKey
import au.com.alfie.ecomm.core.analytics.params.AnalyticsValues.StringValues
import au.com.alfie.ecomm.core.analytics.params.FakeAnalyticsParams
import au.com.alfie.ecomm.core.analytics.params.plus
import au.com.alfie.ecomm.core.analytics.params.toBundle
import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val TEST_SCREEN_NAME = "TestScreen"
private const val EVENT_NAME = "test_event"

@ExtendWith(MockKExtension::class)
class FirebaseAnalyticsProviderTest {

    @RelaxedMockK
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @RelaxedMockK
    private lateinit var analyticsLogger: AnalyticsLogger

    @InjectMockKs
    private lateinit var subject: FirebaseAnalyticsProvider

    @BeforeEach
    fun setup() {
        mockkStatic("au.com.alfie.ecomm.core.analytics.params.AnalyticsParamsExtKt")
    }

    @Test
    fun testTrackEvent() {
        val params = spyk(
            FakeAnalyticsParams(
                mapOf("param" to StringValues("value"))
            )
        )
        val extraParams = mapOf(
            SCREEN_NAME.toFirebaseEventKey() to StringValues(TEST_SCREEN_NAME)
        )
        val paramsJoined = spyk(
            FakeAnalyticsParams(params.params() + extraParams)
        )
        val mockBundle = mockk<Bundle>()

        every { params.plus(extraParams) } returns paramsJoined
        every { paramsJoined.toBundle() } returns mockBundle

        subject.trackEvent(
            screenName = TEST_SCREEN_NAME,
            eventName = EVENT_NAME,
            params = params
        )

        verify {
            analyticsLogger.logEvent(
                tracker = "Firebase",
                event = EVENT_NAME,
                params = mapOf(
                    "param" to "value",
                    "screen_name" to TEST_SCREEN_NAME
                ),
                timestamp = any()
            )

            firebaseAnalytics.logEvent(EVENT_NAME, mockBundle)
        }
    }

    @Test
    fun testTrackError() {
        val eventErrorValue = EventErrorValue.GENERIC_ERROR
        val params = spyk(
            FakeAnalyticsParams(
                mapOf("param" to StringValues("value"))
            )
        )
        val extraParams = mapOf(
            SCREEN_NAME.toFirebaseEventKey() to StringValues(TEST_SCREEN_NAME),
            ERROR_MESSAGE.toFirebaseEventKey() to StringValues(eventErrorValue.toFirebaseEventErrorValue())
        )
        val paramsJoined = spyk(
            FakeAnalyticsParams(params.params() + extraParams)
        )
        val mockBundle = mockk<Bundle>()

        every { params.plus(extraParams) } returns paramsJoined
        every { paramsJoined.toBundle() } returns mockBundle

        subject.trackError(
            screenName = TEST_SCREEN_NAME,
            eventName = EVENT_NAME,
            eventErrorValue = eventErrorValue,
            params = params
        )

        verify {
            analyticsLogger.logEvent(
                tracker = "Firebase",
                event = EVENT_NAME,
                params = mapOf(
                    "param" to "value",
                    "screen_name" to TEST_SCREEN_NAME,
                    "error_message" to "generic_error"
                ),
                timestamp = any()
            )

            firebaseAnalytics.logEvent(EVENT_NAME, mockBundle)
        }
    }
}
