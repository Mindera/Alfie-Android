package au.com.alfie.ecomm.debug.operational.analytics.data

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class AnalyticsLogDataHolderTest {

    @InjectMockKs
    lateinit var subject: AnalyticsLogDataHolder

    @Test
    fun `emit and get - given the data emitted then should get the list with it`() {
        val emitter: AnalyticsLogDataEmitter = subject
        val getter: AnalyticsLogDataGetter = subject

        val data1 = AnalyticsLogData(
            tracker = "tracker1",
            event = "event1",
            params = mapOf("param1" to "value1"),
            timestamp = "21/02/2024 18:55:02"
        )
        val data2 = AnalyticsLogData(
            tracker = "tracker2",
            event = "event2",
            params = mapOf("param2" to "value2"),
            timestamp = "21/02/2024 20:55:02"
        )

        emitter.emit(data1)
        emitter.emit(data2)

        val result = getter.get()

        val expected = listOf(data1, data2)

        assertEquals(expected, result)
    }
}
