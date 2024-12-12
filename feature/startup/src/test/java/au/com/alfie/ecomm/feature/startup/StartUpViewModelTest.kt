package au.com.alfie.ecomm.feature.startup

import app.cash.turbine.test
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.feature.startup.loader.StartUpLoader
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class StartUpViewModelTest {

    @Test
    fun `init - given successful loaders then set Home as start destination`() = runTest {
        val mockLoaders: Set<StartUpLoader> = setOf(
            mockk { coEvery { load() } returns Result.success(Unit) },
            mockk { coEvery { load() } returns Result.success(Unit) }
        )

        val viewModel = buildViewModel(mockLoaders)

        viewModel.startDestination.test {
            assertEquals(Screen.Home, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init - given at least on loader failing then set error start destination`() = runTest {
        val mockLoaders: Set<StartUpLoader> = setOf(
            mockk { coEvery { load() } returns Result.success(Unit) },
            mockk { coEvery { load() } returns Result.failure(Exception()) }
        )

        val viewModel = buildViewModel(mockLoaders)

        viewModel.startDestination.test {
            assertNull(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildViewModel(loaders: Set<StartUpLoader>) = StartUpViewModel(
        loaders = loaders
    )
}
