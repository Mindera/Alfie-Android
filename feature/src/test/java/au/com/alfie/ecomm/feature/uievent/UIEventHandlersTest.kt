package au.com.alfie.ecomm.feature.uievent

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarPriority
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarType
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.Direction
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UIEventHandlersTest {

    @RelaxedMockK
    private lateinit var navigator: DestinationsNavigator

    @RelaxedMockK
    private lateinit var navController: NavController

    @RelaxedMockK
    private lateinit var directionProvider: DirectionProvider

    @RelaxedMockK
    private lateinit var snackbarHostState: SnackbarCustomHostState

    @Test
    fun `handle NavigateToDirection`() = runTest {
        val direction = mockk<Direction>()
        val navOptions: NavOptionsBuilder.() -> Unit = {}
        val event = UIEvent.Base.NavigateToDirection(
            direction = direction,
            navOptions = navOptions
        )

        event.handle()

        verify { navigator.navigate(direction, any(), navOptions) }
    }

    @Test
    fun `handle NavigateToScreen`() = runTest {
        val screen = mockk<Screen>()
        val direction = mockk<Direction>()
        every { directionProvider.fromScreen(screen) } returns direction
        val navOptions: NavOptionsBuilder.() -> Unit = {}
        val event = UIEvent.Base.NavigateToScreen(
            screen = screen,
            navOptions = navOptions
        )

        event.handle()

        verify { navigator.navigate(direction, any(), navOptions) }
    }

    @Test
    fun `handle NavigateToDirectionClearingStack`() = runTest {
        val direction = mockk<Direction>(relaxed = true)
        val event = UIEvent.Base.NavigateToDirectionClearingStack(
            direction = direction,
            clearStartDestination = false,
            launchSingleTop = false,
            saveState = false,
            restoreState = false
        )

        event.handle()

        verify {
            navController.popBackStack(
                destinationId = any(),
                inclusive = any()
            )
            navController.navigate(
                direction = direction,
                navOptionsBuilder = any()
            )
        }
    }

    @Test
    fun `handle NavigateToScreenClearingStack`() = runTest {
        val screen = mockk<Screen>()
        val direction = mockk<Direction>(relaxed = true)
        every { directionProvider.fromScreen(screen) } returns direction
        val event = UIEvent.Base.NavigateToScreenClearingStack(
            screen = screen,
            clearStartDestination = false,
            launchSingleTop = false,
            saveState = false,
            restoreState = false
        )

        event.handle()

        verify {
            navController.popBackStack(
                destinationId = any(),
                inclusive = any()
            )
            navController.navigate(
                direction = direction,
                navOptionsBuilder = any()
            )
        }
    }

    @Test
    fun `handle NavigateBack`() = runTest {
        val event = UIEvent.Base.NavigateBack

        event.handle()

        verify { navigator.navigateUp() }
    }

    @Test
    fun `handle ShowSnackbar`() = runTest {
        val visuals = SnackbarCustomVisuals(
            type = SnackbarType.Info,
            message = "message",
            actionLabel = null,
            withDismissAction = true,
            singleLine = true,
            timeDuration = SnackbarTimeDuration.SHORT,
            priority = SnackbarPriority.NORMAL,
            onActionClick = {}
        )
        val event = UIEvent.Base.ShowSnackbar(visuals = visuals)

        event.handle()

        coVerify { snackbarHostState.showSnackbar(visuals) }
    }

    private suspend fun UIEvent.Base.handle() {
        this.handleBaseEvent(
            navigator = navigator,
            navController = navController,
            directionProvider = directionProvider,
            snackbarHostState = snackbarHostState
        )
    }
}
