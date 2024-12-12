package au.com.alfie.ecomm.debug.operational.analytics

import au.com.alfie.ecomm.core.commons.util.DateUtils
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogData
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataEmitter
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class AnalyticsLoggerOpTest {

    @RelaxedMockK
    lateinit var analyticsLogDataEmitter: AnalyticsLogDataEmitter

    @InjectMockKs
    lateinit var subject: AnalyticsLoggerOp

    @Test
    fun `logEvent - then map the data and emit`() {
        val timestamp = 23402344L

        subject.logEvent(
            tracker = "firebase",
            event = "screen_view",
            params = mapOf("param" to "value"),
            timestamp = timestamp
        )

        val expected = AnalyticsLogData(
            tracker = "firebase",
            event = "screen_view",
            params = mapOf("param" to "value"),
            timestamp = DateUtils.convertMsToDate(timestamp, "dd-MM-yyyy HH:mm:ss")
        )

        verify { analyticsLogDataEmitter.emit(expected) }
    }
}
