package au.com.alfie.ecomm.debug.operational.view.analytics

import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.core.test.TestDispatcherProvider
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogData
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataGetter
import au.com.alfie.ecomm.debug.operational.view.analytics.model.AnalyticsLogState
import au.com.alfie.ecomm.debug.operational.view.analytics.model.AnalyticsLogUI
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class AnalyticsLogViewModelTest {

    @RelaxedMockK
    lateinit var analyticsLogDataGetter: AnalyticsLogDataGetter

    private val testDispatcherProvider = TestDispatcherProvider()

    private lateinit var subject: AnalyticsLogViewModel

    @Test
    fun `init - given data then should set Data state`() = runTest {
        val data = listOf(
            AnalyticsLogData(
                tracker = "tracker1",
                event = "event1",
                params = mapOf("param1" to "value1"),
                timestamp = "21/02/2024 18:55:02"
            ),
            AnalyticsLogData(
                tracker = "tracker2",
                event = "event2",
                params = mapOf("param2" to "value2"),
                timestamp = "21/02/2024 20:55:02"
            )
        )

        every { analyticsLogDataGetter.get() } returns data

        subject = buildSubject()

        val result = subject.state.value

        val expected = AnalyticsLogState.Data(
            AnalyticsLogUI(
                trackers = setOf("All", "tracker1", "tracker2"),
                events = data
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `init - given data empty then should set Empty state`() = runTest {
        every { analyticsLogDataGetter.get() } returns emptyList()

        subject = buildSubject()

        val result = subject.state.value

        val expected = AnalyticsLogState.Empty

        assertEquals(expected, result)
    }

    @Test
    fun `filterByTracker - given a tracker that has events then should set Data state filtered`() = runTest {
        val data = listOf(
            AnalyticsLogData(
                tracker = "tracker1",
                event = "event1",
                params = mapOf("param1" to "value1"),
                timestamp = "21/02/2024 18:55:02"
            ),
            AnalyticsLogData(
                tracker = "tracker2",
                event = "event2",
                params = mapOf("param2" to "value2"),
                timestamp = "21/02/2024 20:55:02"
            )
        )

        every { analyticsLogDataGetter.get() } returns data

        subject = buildSubject()
        subject.filterByTracker("tracker1")

        val result = subject.state.value

        val expected = AnalyticsLogState.Data(
            AnalyticsLogUI(
                trackers = setOf("All", "tracker1", "tracker2"),
                events = listOf(
                    AnalyticsLogData(
                        tracker = "tracker1",
                        event = "event1",
                        params = mapOf("param1" to "value1"),
                        timestamp = "21/02/2024 18:55:02"
                    )
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `filterByTracker - given all trackers then should set Data state with all events`() = runTest {
        val data = listOf(
            AnalyticsLogData(
                tracker = "tracker1",
                event = "event1",
                params = mapOf("param1" to "value1"),
                timestamp = "21/02/2024 18:55:02"
            ),
            AnalyticsLogData(
                tracker = "tracker2",
                event = "event2",
                params = mapOf("param2" to "value2"),
                timestamp = "21/02/2024 20:55:02"
            )
        )

        every { analyticsLogDataGetter.get() } returns data

        subject = buildSubject()

        subject.filterByTracker("tracker1")
        subject.filterByTracker("All")

        val result = subject.state.value

        val expected = AnalyticsLogState.Data(
            AnalyticsLogUI(
                trackers = setOf("All", "tracker1", "tracker2"),
                events = data
            )
        )

        assertEquals(expected, result)
    }

    private fun buildSubject() = AnalyticsLogViewModel(analyticsLogDataGetter, testDispatcherProvider)
}
