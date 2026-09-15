package com.mindera.alfie.feature.scanner.model

internal sealed interface ScannerUIState {

    /**
     * Camera is live. [isTorchOn] drives both the CameraX torch and the header toggle; the manual
     * entry sheet is open when [manualEntry] is non-null, and carries the text typed so far.
     */
    data class Scanning(
        val isTorchOn: Boolean = false,
        val manualEntry: String? = null
    ) : ScannerUIState

    /**
     * A barcode won the single-fire race and navigation is in flight. Terminal for this screen:
     * it also stops the analyzer so ML Kit is not still burning frames during the transition.
     */
    data object Detected : ScannerUIState

    data class Error(val type: ScannerErrorType) : ScannerUIState
}

internal enum class ScannerErrorType {
    /** No camera, or CameraX refused to bind to one. */
    CameraUnavailable
}
