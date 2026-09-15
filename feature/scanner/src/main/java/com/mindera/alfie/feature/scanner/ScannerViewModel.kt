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

    private val _state = MutableStateFlow<ScannerUIState>(ScannerUIState.Scanning())
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
            ScannerEvent.OnTorchToggle -> updateScanning { it.copy(isTorchOn = !it.isTorchOn) }
            ScannerEvent.OnEnterManuallyClick -> updateScanning { it.copy(manualEntry = "") }
            ScannerEvent.OnManualEntryDismiss -> updateScanning { it.copy(manualEntry = null) }
            is ScannerEvent.OnManualBarcodeChange ->
                updateScanning { it.copy(manualEntry = event.value) }
            ScannerEvent.OnManualBarcodeSubmit -> onManualSubmit()
        }
    }

    /** No-op once the screen has left [ScannerUIState.Scanning] — a late tap must not revive it. */
    private fun updateScanning(transform: (ScannerUIState.Scanning) -> ScannerUIState.Scanning) {
        _state.update { current ->
            if (current is ScannerUIState.Scanning) transform(current) else current
        }
    }

    /**
     * A typed barcode is treated exactly like a scanned one — same trimming, same single-fire
     * gate — so the manual path cannot double-navigate alongside a camera hit that lands in the
     * same moment.
     */
    private fun onManualSubmit() {
        val typed = (_state.value as? ScannerUIState.Scanning)?.manualEntry ?: return
        onBarcodeDetected(typed)
    }

    private fun onBarcodeDetected(rawValue: String) {
        val scanned = rawValue.trim()
        if (scanned.isEmpty()) return
        if (!hasNavigated.compareAndSet(false, true)) return

        _state.update { ScannerUIState.Detected }

        // DEMO: the scan itself is real — ML Kit decoded `scanned` off an actual barcode and the
        // single-fire gate above still applies — but the handle is substituted. See
        // DEMO_PRODUCT_HANDLE for why, and remove both to restore the pass-through.
        Timber.i("Scanned '%s'; opening demo product '%s'", scanned, DEMO_PRODUCT_HANDLE)
        val handle = DEMO_PRODUCT_HANDLE
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

    internal companion object {
        /**
         * DEMO ONLY — delete this, and the substitution in [onBarcodeDetected], before any of
         * this goes near main.
         *
         * The BFF cannot resolve a barcode: its whole Query root keys products by handle/slug or
         * free-text search, and `barcodes` exists only as an output field on ProductVariant. So a
         * real EAN scanned off a physical product resolves to nothing and lands on PDP's
         * not-found state — which makes the feature impossible to show end to end. Pinning a
         * known in-stock handle makes the full flow demo-able: scan, navigate, browse variants,
         * add to bag.
         *
         * "t-shirt" is the richest fixture on the local BFF — 9 variants with stock on all of
         * them, 3 colours x 3 sizes, 2 gallery images and a default variant — so the PDP has
         * something to show rather than rendering a bare single-variant product.
         */
        const val DEMO_PRODUCT_HANDLE = "t-shirt"
    }
}
