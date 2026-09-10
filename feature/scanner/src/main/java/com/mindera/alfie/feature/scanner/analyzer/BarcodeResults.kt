package com.mindera.alfie.feature.scanner.analyzer

import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Picks the first barcode in a frame that carries a usable value.
 *
 * A frame can hold several barcodes, and ML Kit reports a detection whose `rawValue` is null when
 * it located a symbol but could not decode it. Trimming matters because some symbologies emit
 * trailing whitespace, and a blank value would go on to build a malformed PDP route.
 *
 * Kept separate from [BarcodeImageAnalyzer] so the decision is testable on the JVM, without a
 * camera or an ML Kit client.
 */
internal fun List<Barcode>.firstUsableRawValue(): String? =
    firstNotNullOfOrNull { barcode -> barcode.rawValue?.trim()?.takeIf(String::isNotEmpty) }
