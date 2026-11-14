package com.mindera.alfie.feature.uievent

import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarTimeDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

// Lives beside the UI event handling because that is what drives it: ShowSnackbar events land here.
@OptIn(ExperimentalCoroutinesApi::class)
class SnackbarCustomHostStateTest {

    // A queueing dispatcher, so snackbars started together really do interleave the way they do on
    // the main thread instead of each running to its first suspension on launch.
    private val dispatcher = StandardTestDispatcher()
    private val hostState = SnackbarCustomHostState()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `showSnackbar - WHEN it is dismissed THEN it returns without waiting out its duration`() = runTest(dispatcher) {
        val shown = launch { hostState.showSnackbar(toast("Added.")) }
        runCurrent()

        hostState.dismissSnackbar()
        runCurrent()

        assertTrue(shown.isCompleted)
    }

    @Test
    fun `showSnackbar - WHEN it is left alone THEN it times out after its duration`() = runTest(dispatcher) {
        val shown = launch { hostState.showSnackbar(toast("Added.")) }

        advanceTimeBy(SnackbarTimeDuration.SHORT.milliseconds - 1)
        assertTrue(shown.isActive)

        advanceTimeBy(2)
        assertTrue(shown.isCompleted)
    }

    @Test
    fun `showSnackbar - WHEN a newer one arrives THEN it replaces the one on screen`() = runTest(dispatcher) {
        val first = launch { hostState.showSnackbar(toast("first")) }
        runCurrent()

        val second = launch { hostState.showSnackbar(toast("second")) }
        runCurrent()

        assertTrue(first.isCompleted)
        assertTrue(second.isActive)
    }

    @Test
    fun `showSnackbar - WHEN two arrive together THEN only the newest is shown, for its full duration`() = runTest(dispatcher) {
        val first = launch { hostState.showSnackbar(toast("first")) }
        runCurrent()

        // Both start in the same dispatch, as two events collected back to back do.
        val second = launch { hostState.showSnackbar(toast("second")) }
        val third = launch { hostState.showSnackbar(toast("third")) }
        runCurrent()

        assertTrue(first.isCompleted)
        assertTrue(second.isCompleted)
        assertTrue(third.isActive)

        // Shown straight away rather than queued behind an orphaned display, so it gets its whole
        // duration on screen and no more.
        advanceTimeBy(SnackbarTimeDuration.SHORT.milliseconds - 1)
        assertTrue(third.isActive)
        advanceTimeBy(2)
        assertTrue(third.isCompleted)
    }

    private fun toast(message: String) = SnackbarCustomVisuals.Toast(message = message)
}
