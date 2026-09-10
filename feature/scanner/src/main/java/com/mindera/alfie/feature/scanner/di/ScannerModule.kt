package com.mindera.alfie.feature.scanner.di

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object ScannerModule {

    /**
     * Restricted to the symbologies retail actually uses rather than `FORMAT_ALL_FORMATS`:
     * a narrower set means faster inference per frame and fewer false positives.
     *
     * Provided rather than constructed inline so [ScannerViewModel] can be unit-tested with a
     * mock. The returned client is [java.io.Closeable] and Hilt will not close it — the
     * ViewModel does, on clear.
     */
    @Provides
    @ViewModelScoped
    fun provideBarcodeScanner(): BarcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_ITF,
                Barcode.FORMAT_QR_CODE
            )
            .build()
    )
}
