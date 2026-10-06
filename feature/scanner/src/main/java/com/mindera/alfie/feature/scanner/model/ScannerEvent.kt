package com.mindera.alfie.feature.scanner.model

internal sealed interface ScannerEvent {

    data object OnCloseClick : ScannerEvent

    data class OnBarcodeDetected(val rawValue: String) : ScannerEvent

    data object OnCameraError : ScannerEvent

    data object OnTorchToggle : ScannerEvent

    data object OnEnterManuallyClick : ScannerEvent

    data object OnManualEntryDismiss : ScannerEvent

    data class OnManualBarcodeChange(val value: String) : ScannerEvent

    data object OnManualBarcodeSubmit : ScannerEvent
}
