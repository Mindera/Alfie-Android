package au.com.alfie.ecomm.debug.nonoperational.analytics

import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class AnalyticsLoggerNonOpTest {

    @Test
    fun `logEvent - then must do nothing`() {
        val subject = spyk(AnalyticsLoggerNonOp())

        subject.logEvent(
            tracker = "tracker",
            event = "event",
            params = mapOf(),
            timestamp = 9784
        )

        verify { subject.logEvent(any(), any(), any(), any()) }
        confirmVerified(subject)
    }
}
