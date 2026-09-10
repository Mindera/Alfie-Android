package com.mindera.alfie.feature.scanner.model

internal sealed interface ScannerEvent {

    data object OnCloseClick : ScannerEvent

    data class OnBarcodeDetected(val rawValue: String) : ScannerEvent

    data object OnCameraError : ScannerEvent
}
