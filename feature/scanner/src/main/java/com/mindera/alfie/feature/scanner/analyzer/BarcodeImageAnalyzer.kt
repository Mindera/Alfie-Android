package com.mindera.alfie.feature.scanner.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage

/**
 * Bridges CameraX frames into ML Kit. Deliberately a thin shim — every decision it could make
 * lives in [firstUsableRawValue] instead, where it can be tested without hardware.
 *
 * [onBarcode] may fire on many consecutive frames for the same physical barcode; de-duplication
 * is the ViewModel's job, not this class's.
 */
internal class BarcodeImageAnalyzer(
    private val scanner: BarcodeScanner,
    private val onBarcode: (String) -> Unit,
    private val onFailure: (Throwable) -> Unit
) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        // rotationDegrees is what lets a barcode scan while the phone is held sideways, even
        // though MainActivity is locked to portrait.
        val input = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(input)
            .addOnSuccessListener { barcodes -> barcodes.firstUsableRawValue()?.let(onBarcode) }
            .addOnFailureListener(onFailure)
            // Exactly once, and only here: closing twice throws, and never closing stalls the
            // pipeline after a handful of frames.
            .addOnCompleteListener { imageProxy.close() }
    }
}
