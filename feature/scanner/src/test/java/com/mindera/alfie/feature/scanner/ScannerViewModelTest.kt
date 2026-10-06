package com.mindera.alfie.feature.scanner

import android.content.Context
import androidx.navigation.navOptions
import app.cash.turbine.test
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.product.GetProductByBarcodeUseCase
import com.mindera.alfie.feature.scanner.destinations.ScannerScreenDestination
import com.mindera.alfie.feature.scanner.model.ScannerErrorType
import com.mindera.alfie.feature.scanner.model.ScannerEvent
import com.mindera.alfie.feature.scanner.model.ScannerLookupError
import com.mindera.alfie.feature.scanner.model.ScannerUIState
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.product.model.BarcodeMatch
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class ScannerViewModelTest {

    @RelaxedMockK
    private lateinit var barcodeScanner: BarcodeScanner

    @RelaxedMockK
    private lateinit var getProductByBarcode: GetProductByBarcodeUseCase

    @RelaxedMockK
    private lateinit var context: Context

    @BeforeEach
    fun setUp() {
        coEvery { getProductByBarcode(any()) } returns UseCaseResult.Success(MATCH)
        every { context.getString(R.string.scanner_barcode_not_found) } returns NOT_FOUND_COPY
        every { context.getString(R.string.scanner_barcode_lookup_failed) } returns FAILED_COPY
    }

    // A real delegate, not a mock: the assertions are about the events it actually emits.
    private fun viewModel() = ScannerViewModel(
        barcodeScanner = barcodeScanner,
        getProductByBarcode = getProductByBarcode,
        context = context,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )

    @Test
    fun `handleEvent - WHEN a barcode resolves THEN navigates to the returned product`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

            val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            // Only the screen is compared: NavigateToScreen carries a navOptions lambda, so
            // whole-event equality would compare function identity and always fail.
            assertEquals(
                Screen.ProductDetails(args = productDetailsNavArgs(handle = MATCH.slug, variantId = MATCH.variantId)),
                event.screen
            )
        }
    }

    // The scan named one variant; carrying it means the PDP opens on the size that was scanned.
    @Test
    fun `handleEvent - WHEN the match names a variant THEN it travels with the navigation`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                assertEquals(
                    Screen.ProductDetails(
                        args = productDetailsNavArgs(handle = MATCH.slug, variantId = "22")
                    ),
                    event.screen
                )
            }
        }

    // The BFF nulls variantId when no single variant carries the code; the PDP then picks its own.
    @Test
    fun `handleEvent - WHEN the match has no variant THEN the PDP is opened without one`() =
        runTest {
            coEvery { getProductByBarcode(any()) } returns
                UseCaseResult.Success(MATCH.copy(variantId = null))
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                assertEquals(
                    Screen.ProductDetails(
                        args = productDetailsNavArgs(handle = MATCH.slug, variantId = null)
                    ),
                    event.screen
                )
            }
        }

    @Test
    fun `handleEvent - WHEN a barcode is detected THEN the scanned value is what is looked up`() =
        runTest {
            viewModel().handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

            coVerify(exactly = 1) { getProductByBarcode(BARCODE) }
        }

    @Test
    fun `handleEvent - WHEN the barcode matches nothing THEN it says so and keeps scanning`() =
        runTest {
            coEvery { getProductByBarcode(any()) } returns
                UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

                val event = assertIs<UIEvent.Base.ShowSnackbar>(awaitItem())
                assertEquals(SnackbarType.Error, event.visuals.type)
                assertEquals(NOT_FOUND_COPY, event.visuals.message)
            }

            assertEquals(ScannerUIState.Scanning(), viewModel.state.value)
        }

    // A transport or server fault is not the shopper's scan being wrong, so it gets its own copy.
    @Test
    fun `handleEvent - WHEN the lookup fails THEN a generic error is shown`() = runTest {
        coEvery { getProductByBarcode(any()) } returns
            UseCaseResult.Error(ErrorResult(type = ErrorType.NETWORK))
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

            val event = assertIs<UIEvent.Base.ShowSnackbar>(awaitItem())
            assertEquals(FAILED_COPY, event.visuals.message)
        }
    }

    // The point of releasing the gate on failure: the shopper's next scan must still work.
    @Test
    fun `handleEvent - WHEN a failed scan is followed by a good one THEN it navigates`() = runTest {
        coEvery { getProductByBarcode(BARCODE) } returns
            UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
        coEvery { getProductByBarcode(OTHER_BARCODE) } returns UseCaseResult.Success(MATCH)
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
            assertIs<UIEvent.Base.ShowSnackbar>(awaitItem())

            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(OTHER_BARCODE))

            val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            assertEquals(
                Screen.ProductDetails(args = productDetailsNavArgs(handle = MATCH.slug, variantId = MATCH.variantId)),
                event.screen
            )
        }
    }

    @Test
    fun `handleEvent - WHEN a failed scan had the torch on THEN the torch survives it`() = runTest {
        coEvery { getProductByBarcode(any()) } returns
            UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
        val viewModel = viewModel()

        viewModel.handleEvent(ScannerEvent.OnTorchToggle)
        viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

        assertEquals(ScannerUIState.Scanning(isTorchOn = true), viewModel.state.value)
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

            coVerify(exactly = 1) { getProductByBarcode(any()) }
        }

    @Test
    fun `handleEvent - WHEN a second different barcode is detected THEN it is ignored`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(OTHER_BARCODE))

            assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
            expectNoEvents()
        }

        coVerify(exactly = 0) { getProductByBarcode(OTHER_BARCODE) }
    }

    @Test
    fun `handleEvent - WHEN the barcode is padded with whitespace THEN it is trimmed`() = runTest {
        viewModel().handleEvent(ScannerEvent.OnBarcodeDetected("  $BARCODE  "))

        coVerify(exactly = 1) { getProductByBarcode(BARCODE) }
    }

    @Test
    fun `handleEvent - WHEN the barcode is blank THEN nothing is looked up`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(""))
            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected("   "))

            expectNoEvents()
        }

        coVerify(exactly = 0) { getProductByBarcode(any()) }
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
    fun `handleEvent - WHEN the camera fails to bind THEN the state reports it unavailable`() =
        runTest {
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnCameraError)

            assertEquals(
                ScannerUIState.Error(ScannerErrorType.CameraUnavailable),
                viewModel.state.value
            )
        }

    // A frame already in flight when the camera failed must not pull the screen back out of its
    // error state.
    @Test
    fun `handleEvent - WHEN a barcode lands after a camera error THEN it is ignored`() = runTest {
        val viewModel = viewModel()

        viewModel.handleEvent(ScannerEvent.OnCameraError)
        viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))

        assertEquals(
            ScannerUIState.Error(ScannerErrorType.CameraUnavailable),
            viewModel.state.value
        )
        coVerify(exactly = 0) { getProductByBarcode(any()) }
    }

    @Test
    fun `state - WHEN created THEN starts scanning with the torch off and no manual entry`() {
        assertEquals(ScannerUIState.Scanning(), viewModel().state.value)
    }

    @Test
    fun `handleEvent - WHEN the torch is toggled THEN it flips and flips back`() {
        val viewModel = viewModel()

        viewModel.handleEvent(ScannerEvent.OnTorchToggle)
        assertEquals(ScannerUIState.Scanning(isTorchOn = true), viewModel.state.value)

        viewModel.handleEvent(ScannerEvent.OnTorchToggle)
        assertEquals(ScannerUIState.Scanning(isTorchOn = false), viewModel.state.value)
    }

    @Test
    fun `handleEvent - WHEN manual entry is opened typed and dismissed THEN state follows`() {
        val viewModel = viewModel()

        viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
        assertEquals(ScannerUIState.Scanning(manualEntry = ""), viewModel.state.value)

        viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
        assertEquals(ScannerUIState.Scanning(manualEntry = BARCODE), viewModel.state.value)

        viewModel.handleEvent(ScannerEvent.OnManualEntryDismiss)
        assertEquals(ScannerUIState.Scanning(manualEntry = null), viewModel.state.value)
    }

    @Test
    fun `handleEvent - WHEN a barcode is typed and submitted THEN it resolves like a scan`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)

                val event = assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())
                assertEquals(
                    Screen.ProductDetails(args = productDetailsNavArgs(handle = MATCH.slug, variantId = MATCH.variantId)),
                    event.screen
                )
            }

            coVerify(exactly = 1) { getProductByBarcode(BARCODE) }
        }

    /**
     * The sheet and its keyboard cover the snackbar, so a typed code that fails is answered on the
     * field instead — and the typed value is kept, because retyping would be the wrong penalty.
     */
    @Test
    fun `handleEvent - WHEN a typed barcode matches nothing THEN the field carries the error`() =
        runTest {
            coEvery { getProductByBarcode(any()) } returns
                UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)

                // No snackbar: it would render behind the sheet.
                expectNoEvents()
            }

            assertEquals(
                ScannerUIState.Scanning(
                    manualEntry = BARCODE,
                    manualError = ScannerLookupError.NotFound
                ),
                viewModel.state.value
            )
        }

    @Test
    fun `handleEvent - WHEN a typed lookup fails THEN the field carries the generic error`() =
        runTest {
            coEvery { getProductByBarcode(any()) } returns
                UseCaseResult.Error(ErrorResult(type = ErrorType.NETWORK))
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)

            assertEquals(
                ScannerLookupError.LookupFailed,
                (viewModel.state.value as ScannerUIState.Scanning).manualError
            )
        }

    @Test
    fun `handleEvent - WHEN the shopper edits after a failed manual lookup THEN the error clears`() =
        runTest {
            coEvery { getProductByBarcode(any()) } returns
                UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(OTHER_BARCODE))

            assertEquals(
                ScannerUIState.Scanning(manualEntry = OTHER_BARCODE),
                viewModel.state.value
            )
        }

    // The screen renders the sheet from the resume state, so this is what keeps it up meanwhile.
    @Test
    fun `handleEvent - WHEN a typed lookup is in flight THEN the entry is kept for the sheet`() =
        runTest {
            val lookup = CompletableDeferred<UseCaseResult<BarcodeMatch>>()
            coEvery { getProductByBarcode(any()) } coAnswers { lookup.await() }
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)

            assertEquals(
                ScannerUIState.Searching(resume = ScannerUIState.Scanning(manualEntry = BARCODE)),
                viewModel.state.value
            )
            lookup.cancel()
        }

    @Test
    fun `handleEvent - WHEN the sheet is dismissed mid-lookup and it fails THEN a snackbar says so`() =
        runTest {
            val lookup = CompletableDeferred<UseCaseResult<BarcodeMatch>>()
            coEvery { getProductByBarcode(any()) } coAnswers { lookup.await() }
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeChange(BARCODE))
                viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)
                viewModel.handleEvent(ScannerEvent.OnManualEntryDismiss)
                lookup.complete(UseCaseResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND)))

                val event = assertIs<UIEvent.Base.ShowSnackbar>(awaitItem())
                assertEquals(NOT_FOUND_COPY, event.visuals.message)
            }

            // The sheet stays closed rather than reopening to carry the error.
            assertEquals(ScannerUIState.Scanning(), viewModel.state.value)
        }

    @Test
    fun `messageRes - WHEN mapping lookup errors THEN each gets its own copy`() {
        assertEquals(R.string.scanner_barcode_not_found, ScannerLookupError.NotFound.messageRes())
        assertEquals(
            R.string.scanner_barcode_lookup_failed,
            ScannerLookupError.LookupFailed.messageRes()
        )
    }

    @Test
    fun `handleEvent - WHEN manual entry is submitted empty THEN nothing is emitted`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(ScannerEvent.OnEnterManuallyClick)
            viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)

            expectNoEvents()
        }
    }

    // The camera can win while the sheet is open; the single-flight gate is shared, so the typed
    // value must not produce a second lookup.
    @Test
    fun `handleEvent - WHEN a scan already navigated THEN a later manual submit is ignored`() =
        runTest {
            val viewModel = viewModel()

            viewModel.uiEvent.test {
                viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
                assertIs<UIEvent.Base.NavigateToScreen>(awaitItem())

                viewModel.handleEvent(ScannerEvent.OnManualBarcodeSubmit)
                expectNoEvents()
            }
        }

    @Test
    fun `handleEvent - WHEN the torch is toggled after a match THEN state does not revive`() =
        runTest {
            val viewModel = viewModel()

            viewModel.handleEvent(ScannerEvent.OnBarcodeDetected(BARCODE))
            viewModel.handleEvent(ScannerEvent.OnTorchToggle)

            assertIs<ScannerUIState.Searching>(viewModel.state.value)
        }

    @Test
    fun `releaseScanner - WHEN called THEN closes the ML Kit client`() {
        viewModel().releaseScanner()

        verify(exactly = 1) { barcodeScanner.close() }
    }

    private companion object {
        const val BARCODE = "5012345678900"
        const val OTHER_BARCODE = "9780000000000"
        const val NOT_FOUND_COPY = "No product matches that barcode."
        const val FAILED_COPY = "Couldn't check that barcode. Please try again."

        val MATCH = BarcodeMatch(
            id = "8",
            name = "Levi 501",
            slug = "levi-501-8",
            variantId = "22"
        )
    }
}
