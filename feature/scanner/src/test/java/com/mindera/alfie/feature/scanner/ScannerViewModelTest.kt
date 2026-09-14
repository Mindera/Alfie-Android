package com.mindera.alfie.feature.scanner

import androidx.navigation.navOptions
import app.cash.turbine.test
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.feature.scanner.destinations.ScannerScreenDestination
import com.mindera.alfie.feature.scanner.model.ScannerErrorType
import com.mindera.alfie.feature.scanner.model.ScannerEvent
import com.mindera.alfie.feature.scanner.model.ScannerUIState
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class ScannerViewModelTest {

    @RelaxedMockK
    private lateinit var barcodeScanner: BarcodeScanner

    // A real delegate, not a mock: the assertions are about the events it actually emits.
    private fun viewModel() = ScannerViewModel(
        barcodeScanner = barcodeScanner,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )

    // DEMO: the scanned value is deliberately discarded in favour of a known in-stock handle,
    // because the BFF cannot resolve a barcode. Restore the pass-through assertions alongside
    // ScannerViewModel.DEMO_PRODUCT_HANDLE.
    @Test
    fun `handleEvent - WHEN a barcode is detected THEN navigates to the demo product`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

            val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            // Only the screen is compared: NavigateToScreen carries a navOptions lambda, so
            // whole-event equality would compare function identity and always fail.
            assertEquals(
                Screen.ProductDetails(
                    args = productDetailsNavArgs(handle = ScannerViewModel.DEMO_PRODUCT_HANDLE)
                ),
                event.screen
            )
        }
    }

    @Test
    fun `handleEvent - WHEN an unrelated barcode is scanned THEN it still opens the demo product`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("not-a-real-slug-0000"))

                val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                assertEquals(
                    Screen.ProductDetails(
                        args = productDetailsNavArgs(handle = ScannerViewModel.DEMO_PRODUCT_HANDLE)
                    ),
                    event.screen
                )
            }
        }

    @Test
    fun `handleEvent - WHEN the same barcode is detected repeatedly THEN navigates exactly once`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                expectNoEvents()
            }
        }

    @Test
    fun `handleEvent - WHEN a second different barcode is detected THEN it is ignored`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("9780000000000"))

            val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            assertEquals(
                Screen.ProductDetails(
                    args = productDetailsNavArgs(handle = ScannerViewModel.DEMO_PRODUCT_HANDLE)
                ),
                event.screen
            )
            expectNoEvents()
        }
    }

    @Test
    fun `handleEvent - WHEN the barcode is padded with whitespace THEN it still counts as a scan`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("  $BARCODE  "))

                assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            }
        }

    @Test
    fun `handleEvent - WHEN the barcode is blank THEN nothing is emitted`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(""))
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("   "))

            expectNoEvents()
        }
    }

    @Test
    fun `handleEvent - WHEN a blank barcode was seen first THEN a later real one still navigates`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("   "))
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            }
        }

    /**
     * The back-stack contract, asserted rather than assumed: the scanner must pop itself in the
     * same transaction that pushes the PDP, so pressing back from the PDP returns to Home instead
     * of reopening the camera. The navOptions lambda is applied to a real builder here because
     * comparing the lambda itself would only compare function identity.
     */
    @Test
    fun `handleEvent - WHEN navigating to the PDP THEN the scanner is popped in the same transaction`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                val options = navOptions(event.navOptions)

                assertEquals(ScannerScreenDestination.route, options.popUpToRoute)
                assertTrue(options.isPopUpToInclusive(), "the scanner must not stay on the stack")
                assertTrue(options.shouldLaunchSingleTop(), "a repeat scan must not stack PDPs")
            }
        }

    @Test
    fun `handleEvent - WHEN close is clicked THEN navigates back`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnCloseClick)

            assertEquals(UIEvent.Base.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `handleEvent - WHEN a barcode is detected THEN the state stops the analyzer`() = runTest {
        val viewModel = viewModel()

        viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

        assertEquals(ScannerUIState.Detected, viewModel.state.value)
    }

    @Test
    fun `handleEvent - WHEN the camera fails to bind THEN the state reports it unavailable`() =
        runTest {
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnCameraError)

            assertEquals(
                ScannerUIState.Error(ScannerErrorType.CameraUnavailable),
                viewModel.state.value
            )
        }

    @Test
    fun `state - WHEN created THEN starts scanning`() {
        assertEquals(ScannerUIState.Scanning, viewModel().state.value)
    }

    @Test
    fun `releaseScanner - WHEN called THEN closes the ML Kit client`() {
        viewModel().releaseScanner()

        verify(exactly = 1) { barcodeScanner.close() }
    }

    private companion object {
        const val BARCODE = "5012345678900"
    }
}
