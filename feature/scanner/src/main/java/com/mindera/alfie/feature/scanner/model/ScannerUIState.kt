package com.mindera.alfie.feature.scanner.model

internal sealed interface ScannerUIState {

    /**
     * Camera is live. [isTorchOn] drives both the CameraX torch and the header toggle; the manual
     * entry sheet is open when [manualEntry] is non-null, and carries the text typed so far.
     *
     * [manualError] is how a failed lookup is reported *while the sheet is open*, because the
     * sheet and its keyboard cover the snackbar. It only ever accompanies a non-null
     * [manualEntry], and typing clears it.
     */
    data class Scanning(
        val isTorchOn: Boolean = false,
        val manualEntry: String? = null,
        val manualError: ScannerLookupError? = null
    ) : ScannerUIState

    /**
     * A barcode won the single-fire race and the BFF lookup for it is in flight. The analyzer is
     * stopped for the duration, so ML Kit is not burning frames on a code already being resolved.
     *
     * [resume] is the [Scanning] state to fall back to when the lookup finds nothing or fails, so
     * a failed scan leaves the torch — and any half-typed manual entry — exactly as it was.
     */
    data class Searching(val resume: Scanning) : ScannerUIState

    data class Error(val type: ScannerErrorType) : ScannerUIState
}

internal enum class ScannerErrorType {
    /** No camera, or CameraX refused to bind to one. */
    CameraUnavailable
}

/** Why a barcode did not open a product. */
internal enum class ScannerLookupError {
    /** The scan worked; the catalogue carries no product for that code. */
    NotFound,

    /** The request itself failed — network, timeout or a server fault. */
    LookupFailed
}
