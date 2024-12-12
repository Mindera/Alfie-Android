package au.com.alfie.ecomm.core.analytics.events

import au.com.alfie.ecomm.core.analytics.AnalyticsManager
import au.com.alfie.ecomm.core.analytics.params.FakeAnalyticsParams
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TrackEventTest {

    @RelaxedMockK
    lateinit var analyticsManager: AnalyticsManager

    @Test
    fun `track - then pass the correct data`() {
        val subject = FakeTrackEvent()
        val params = FakeAnalyticsParams()

        subject.track(params)

        verify {
            analyticsManager.trackEvent(
                screenName = "Home",
                eventName = "screen_view",
                params = params
            )
        }
    }

    inner class FakeTrackEvent : TrackEvent(
        screenName = "Home",
        eventName = "screen_view",
        analyticsManager = analyticsManager
    )
}
