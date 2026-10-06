package com.mindera.alfie.feature.scanner

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.product.GetProductByBarcodeUseCase
import com.mindera.alfie.feature.scanner.analyzer.BarcodeImageAnalyzer
import com.mindera.alfie.feature.scanner.destinations.ScannerScreenDestination
import com.mindera.alfie.feature.scanner.model.ScannerErrorType
import com.mindera.alfie.feature.scanner.model.ScannerEvent
import com.mindera.alfie.feature.scanner.model.ScannerLookupError
import com.mindera.alfie.feature.scanner.model.ScannerUIState
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.product.model.BarcodeMatch
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val barcodeScanner: BarcodeScanner,
    private val getProductByBarcode: GetProductByBarcodeUseCase,
    @ApplicationContext private val context: Context,
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
     * This gates the lookup to one in flight at a time: `compareAndSet` is what makes that safe
     * off the main thread, where a plain `Boolean` would race. Unlike a scan that navigates away,
     * a lookup that finds nothing releases the gate so the next code can be tried.
     */
    private val isResolving = AtomicBoolean(false)

    fun handleEvent(event: ScannerEvent) {
        when (event) {
            is ScannerEvent.OnBarcodeDetected -> onBarcodeDetected(event.rawValue)
            ScannerEvent.OnCloseClick -> navigateBack()
            ScannerEvent.OnCameraError -> onCameraError()
            ScannerEvent.OnTorchToggle -> updateScanning { it.copy(isTorchOn = !it.isTorchOn) }
            ScannerEvent.OnEnterManuallyClick ->
                updateScanning { it.copy(manualEntry = "", manualError = null) }
            ScannerEvent.OnManualEntryDismiss -> dismissManualEntry()
            is ScannerEvent.OnManualBarcodeChange ->
                updateScanning { it.copy(manualEntry = event.value, manualError = null) }
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
     * The sheet stays up while a typed lookup is in flight, so it can also be dismissed then. That
     * has to land in the state the lookup will resume to, or the sheet would reopen on failure.
     */
    private fun dismissManualEntry() {
        _state.update { current ->
            when (current) {
                is ScannerUIState.Scanning -> current.copy(manualEntry = null, manualError = null)
                is ScannerUIState.Searching -> current.copy(
                    resume = current.resume.copy(manualEntry = null, manualError = null)
                )
                is ScannerUIState.Error -> current
            }
        }
    }

    /**
     * A typed barcode is treated exactly like a scanned one — same trimming, same single-flight
     * gate — so the manual path cannot double-resolve alongside a camera hit that lands in the
     * same moment.
     */
    private fun onManualSubmit() {
        val typed = (_state.value as? ScannerUIState.Scanning)?.manualEntry ?: return
        onBarcodeDetected(typed)
    }

    private fun onBarcodeDetected(rawValue: String) {
        val scanned = rawValue.trim()
        if (scanned.isEmpty()) return
        if (!isResolving.compareAndSet(false, true)) return

        // Only a live camera can start a lookup; an error state must not be pulled out of itself
        // by a frame that was already in flight when it was entered.
        val resume = _state.value as? ScannerUIState.Scanning
        if (resume == null) {
            isResolving.set(false)
            return
        }

        _state.update { ScannerUIState.Searching(resume = resume) }

        viewModelScope.launch {
            getProductByBarcode(barcode = scanned).doOnResult(
                onSuccess = { match -> openProduct(match) },
                onError = { error -> onLookupFailed(barcode = scanned, error = error) }
            )
        }
    }

    /**
     * The scanned code identifies one specific variant, so it is carried to the PDP — the shopper
     * scanned a size off a physical tag and should not have to pick it again. It is null when the
     * BFF could not narrow the code to a single variant, and the PDP falls back to its default.
     */
    private fun openProduct(match: BarcodeMatch) {
        Timber.i(
            "Barcode resolved to '%s' (%s), variant %s",
            match.slug,
            match.name,
            match.variantId ?: "unknown"
        )
        navigateTo(
            screen = Screen.ProductDetails(
                args = productDetailsNavArgs(
                    handle = match.slug,
                    variantId = match.variantId
                )
            ),
            navOptions = {
                launchSingleTop = true
                // Pop the camera and push the PDP in one transaction, so back from the PDP
                // returns to Home rather than reopening the scanner.
                popUpTo(ScannerScreenDestination.route) { inclusive = true }
            }
        )
    }

    /**
     * A code the catalogue does not carry is the common case, not a fault, so it gets its own copy
     * — telling the shopper the scan worked and the product did not, rather than blaming the app.
     * Either way the camera comes back, because the next thing they will do is scan again.
     *
     * Where the message goes depends on where the code came from. A snackbar is right for a
     * camera scan, but the manual sheet and its keyboard cover the snackbar entirely, so a typed
     * code is answered on the field itself instead — as long as the sheet is still open. The
     * resume state is read at failure time, not at submit, because the sheet can be dismissed
     * while the lookup is in flight; the answer then falls back to the snackbar.
     */
    private fun onLookupFailed(barcode: String, error: ErrorResult) {
        Timber.w(error, "Lookup failed for barcode '%s' (%s)", barcode, error.type)

        val lookupError = if (error.type == ErrorType.RESOURCE_NOT_FOUND) {
            ScannerLookupError.NotFound
        } else {
            ScannerLookupError.LookupFailed
        }
        val searching = _state.value as? ScannerUIState.Searching
        val isManual = searching?.resume?.manualEntry != null

        _state.update { current ->
            when {
                current !is ScannerUIState.Searching -> current
                isManual -> current.resume.copy(manualError = lookupError)
                else -> current.resume
            }
        }
        isResolving.set(false)

        if (isManual || searching == null) return

        showSnackbar(
            SnackbarCustomVisuals.Snackbar(
                type = SnackbarType.Error,
                message = context.getString(lookupError.messageRes()),
                singleLine = false
            )
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

/** The copy for a failed lookup, shared by the snackbar and the manual sheet's error text. */
@StringRes
internal fun ScannerLookupError.messageRes(): Int = when (this) {
    ScannerLookupError.NotFound -> R.string.scanner_barcode_not_found
    ScannerLookupError.LookupFailed -> R.string.scanner_barcode_lookup_failed
}
