package com.mindera.alfie.feature.scanner

import androidx.annotation.VisibleForTesting
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.feature.scanner.analyzer.BarcodeImageAnalyzer
import com.mindera.alfie.feature.scanner.destinations.ScannerScreenDestination
import com.mindera.alfie.feature.scanner.model.ScannerErrorType
import com.mindera.alfie.feature.scanner.model.ScannerEvent
import com.mindera.alfie.feature.scanner.model.ScannerUIState
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val barcodeScanner: BarcodeScanner,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val _state = MutableStateFlow<ScannerUIState>(ScannerUIState.Scanning)
    val state = _state.asStateFlow()

    /**
     * Owned here because the ML Kit client it wraps is owned here — the two share a lifetime, and
     * splitting them risks handing the camera an analyzer whose scanner has already been closed.
     * Implementing [ImageAnalysis.Analyzer] touches no Android framework code, so this stays
     * unit-testable.
     */
    val analyzer: ImageAnalysis.Analyzer = BarcodeImageAnalyzer(
        scanner = barcodeScanner,
        onBarcode = { handleEvent(ScannerEvent.OnBarcodeDetected(it)) },
        onFailure = { Timber.w(it, "Barcode detection failed for a frame") }
    )

    /**
     * The analyzer reports the same physical barcode on frame after frame, from a camera thread.
     * This is a first-wins gate rather than a time-based debounce: the requirement is one
     * navigation per scanner visit, and `compareAndSet` is what makes that safe off the main
     * thread, where a plain `Boolean` would race.
     */
    private val hasNavigated = AtomicBoolean(false)

    fun handleEvent(event: ScannerEvent) {
        when (event) {
            is ScannerEvent.OnBarcodeDetected -> onBarcodeDetected(event.rawValue)
            ScannerEvent.OnCloseClick -> navigateBack()
            ScannerEvent.OnCameraError -> onCameraError()
        }
    }

    private fun onBarcodeDetected(rawValue: String) {
        val handle = rawValue.trim()
        if (handle.isEmpty()) return
        if (!hasNavigated.compareAndSet(false, true)) return

        _state.update { ScannerUIState.Detected }

        // The scanned value is passed straight through as the PDP handle. Barcodes are not
        // product slugs, so a real EAN will usually land on PDP's not-found state until the BFF
        // exposes a barcode lookup — see the feature plan.
        navigateTo(
            screen = Screen.ProductDetails(args = productDetailsNavArgs(handle = handle)),
            navOptions = {
                launchSingleTop = true
                // Pop the camera and push the PDP in one transaction, so back from the PDP
                // returns to Home rather than reopening the scanner.
                popUpTo(ScannerScreenDestination.route) { inclusive = true }
            }
        )
    }

    private fun onCameraError() {
        _state.update { ScannerUIState.Error(ScannerErrorType.CameraUnavailable) }
    }

    override fun onCleared() {
        releaseScanner()
        super.onCleared()
    }

    /** Hilt provides the ML Kit client but never closes it; [onCleared] is where that happens. */
    @VisibleForTesting
    internal fun releaseScanner() {
        barcodeScanner.close()
    }
}
