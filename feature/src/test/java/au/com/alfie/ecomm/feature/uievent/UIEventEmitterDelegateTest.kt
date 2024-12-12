package au.com.alfie.ecomm.feature.uievent

import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptionsBuilder
import app.cash.turbine.test
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarType
import com.ramcosta.composedestinations.spec.Direction
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class UIEventEmitterDelegateTest {

    @InjectMockKs
    private lateinit var delegate: UIEventEmitterDelegate

    @Test
    fun `emitUIEvent - successfully emits the ui event`() = runTest {
        val event = object : UIEvent.Custom {}
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                emitUIEvent(event)

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `navigateTo - successfully emits NavigateToDirection event`() = runTest {
        val direction = mockk<Direction>()
        val navOptions: NavOptionsBuilder.() -> Unit = {}
        val event = UIEvent.Base.NavigateToDirection(
            direction = direction,
            navOptions = navOptions
        )
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                navigateTo(
                    direction = direction,
                    navOptions = navOptions
                )

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `navigateTo - successfully emits NavigateToScreen event`() = runTest {
        val screen = mockk<Screen>()
        val navOptions: NavOptionsBuilder.() -> Unit = {}
        val event = UIEvent.Base.NavigateToScreen(
            screen = screen,
            navOptions = navOptions
        )
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                navigateTo(
                    screen = screen,
                    navOptions = navOptions
                )

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `navigateClearingStack - successfully emits NavigateToDirectionClearingStack event`() = runTest {
        val direction = mockk<Direction>()
        val event = UIEvent.Base.NavigateToDirectionClearingStack(direction = direction)
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                navigateClearingStack(direction = direction)

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `navigateClearingStack - successfully emits NavigateToScreenClearingStack event`() = runTest {
        val screen = mockk<Screen>()
        val event = UIEvent.Base.NavigateToScreenClearingStack(screen = screen)
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                navigateClearingStack(screen = screen)

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `navigateBack - successfully emits NavigateBack event`() = runTest {
        val event = UIEvent.Base.NavigateBack
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                navigateBack()

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    @Test
    fun `showSnackbar - successfully emits ShowSnackbar event`() = runTest {
        val visuals = SnackbarCustomVisuals(
            type = SnackbarType.Info,
            message = "message"
        )
        val event = UIEvent.Base.ShowSnackbar(visuals = visuals)
        val viewModel = TestViewModel(delegate)
        with(viewModel) {
            uiEvent.test {
                showSnackbar(visuals = visuals)

                val result = expectMostRecentItem()
                assertEquals(event, result)
            }
        }
    }

    private class TestViewModel(
        uiEventEmitterDelegate: UIEventEmitterDelegate
    ) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate
}
