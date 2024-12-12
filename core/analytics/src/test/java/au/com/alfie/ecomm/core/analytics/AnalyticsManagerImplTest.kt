package au.com.alfie.ecomm.core.analytics

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue
import au.com.alfie.ecomm.core.analytics.params.AnalyticsParams
import au.com.alfie.ecomm.core.analytics.providers.FirebaseAnalyticsProvider
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class AnalyticsManagerImplTest {

    @MockK
    private lateinit var firebaseAnalyticsProvider: FirebaseAnalyticsProvider

    @RelaxedMockK
    private lateinit var params: AnalyticsParams

    @InjectMockKs
    private lateinit var subject: AnalyticsManagerImpl

    companion object {
        private const val TEST_SCREEN_NAME = "TestScreen"
        private const val EVENT_NAME = "test_event"
    }

    @Test
    fun `WHEN trackEvent is called THEN provider should be called`() {
        every { firebaseAnalyticsProvider.trackEvent(any(), any(), any()) } just runs

        subject.trackEvent(
            screenName = TEST_SCREEN_NAME,
            eventName = EVENT_NAME,
            params = params
        )

        verify {
            firebaseAnalyticsProvider.trackEvent(
                screenName = TEST_SCREEN_NAME,
                eventName = EVENT_NAME,
                params = params
            )
        }
    }

    @Test
    fun `WHEN trackError is called THEN provider should be called`() {
        every { firebaseAnalyticsProvider.trackError(any(), any(), any(), any()) } just runs

        subject.trackError(
            screenName = TEST_SCREEN_NAME,
            eventName = EVENT_NAME,
            eventErrorValue = EventErrorValue.GENERIC_ERROR,
            params = params
        )

        verify {
            firebaseAnalyticsProvider.trackError(
                screenName = TEST_SCREEN_NAME,
                eventName = EVENT_NAME,
                eventErrorValue = EventErrorValue.GENERIC_ERROR,
                params = params
            )
        }
    }
}
