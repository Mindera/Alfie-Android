package com.mindera.alfie.feature.scanner.analyzer

import com.google.mlkit.vision.barcode.common.Barcode
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class BarcodeResultsTest {

    @Test
    fun `firstUsableRawValue - WHEN the frame has no barcodes THEN returns null`() {
        assertNull(emptyList<Barcode>().firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN a barcode was located but not decoded THEN returns null`() {
        assertNull(listOf(barcode(null), barcode(null)).firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN a barcode decodes THEN returns its value`() {
        assertEquals("5012345678900", listOf(barcode("5012345678900")).firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN an undecoded barcode comes first THEN skips to the usable one`() {
        val barcodes = listOf(barcode(null), barcode("5012345678900"))

        assertEquals("5012345678900", barcodes.firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN several decode THEN returns the first`() {
        val barcodes = listOf(barcode("first"), barcode("second"))

        assertEquals("first", barcodes.firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN a value is whitespace only THEN it is skipped`() {
        val barcodes = listOf(barcode("   "), barcode("5012345678900"))

        assertEquals("5012345678900", barcodes.firstUsableRawValue())
    }

    @Test
    fun `firstUsableRawValue - WHEN a value has surrounding whitespace THEN it is trimmed`() {
        assertEquals("5012345678900", listOf(barcode("  5012345678900 ")).firstUsableRawValue())
    }

    private fun barcode(rawValue: String?): Barcode = mockk {
        every { this@mockk.rawValue } returns rawValue
    }
}
