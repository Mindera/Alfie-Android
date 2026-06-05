package com.mindera.alfie.feature.pdp

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class ProductDetailsUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @RelaxedMockK
    private lateinit var environmentManager: EnvironmentManager

    @InjectMockKs
    private lateinit var uiFactory: ProductDetailsUIFactory

    @BeforeEach
    fun setup() {
        every { dispatcherProvider.default() } returns Dispatchers.Main
        coEvery { environmentManager.current() } returns Environment.Prod(graphQLUrl = "", webUrl = BASE_URL)
    }

    @Test
    fun `invoke - returns product details with reconstructed colors and sizes from variant options`() = runTest {
        val result = uiFactory(product)

        assertEquals("Camilla and Marc", result.brand)
        assertEquals("Seamless sculpt mid thigh short", result.name)
        assertEquals(2, result.colors.size)
        assertEquals("steel", result.selectedColorUI?.id)
        assertNotNull(result.gallery)
        assertFalse(result.isSelectionSoldOut)

        val sizeSelector = result.sizeSectionUI as SizeSectionUI.SizeSelector
        // For the "steel" color, sizes "10 AU" and "6 AU" should be available.
        assertEquals(2, sizeSelector.sizes.size)
    }

    @Test
    fun `setSelectedColour - returns updated product details with new size section`() = runTest {
        val result = uiFactory(product)
        val updatedResult = uiFactory.setSelectedColour(details = result, index = 1)

        assertEquals("bone", updatedResult.selectedColorUI?.id)

        val sizeSelector = updatedResult.sizeSectionUI as SizeSectionUI.SizeSelector
        // For "bone": "11 AU" (sold out) and "12 AU" (available).
        assertEquals(2, sizeSelector.sizes.size)
        val outOfStock = sizeSelector.sizes.first { it.id == "11 AU" }
        assertEquals(SizingButtonState.OutOfStock, outOfStock.properties.state)
    }

    @Test
    fun `setSelectedSize - returns updated product details with selected size`() = runTest {
        val result = uiFactory(product)
        val updatedResult = uiFactory.setSelectedSize(details = result, sizeUI = sizeUI)

        val sizeSelector = updatedResult.sizeSectionUI as SizeSectionUI.SizeSelector
        assertEquals(sizeUI, sizeSelector.selectedSize)
    }

    @Test
    fun `invoke - WHEN selection is out of stock THEN bag button is not enabled`() = runTest {
        // Make all "steel" variants unavailable.
        val updatedVariants = product.variants.map { entry ->
            val color = entry.options.firstOrNull { it.name.equals("color", ignoreCase = true) }?.value
            entry.copy(available = color != "steel")
        }
        val updatedProduct = product.copy(variants = updatedVariants)
        var result = uiFactory(updatedProduct)

        assertTrue(result.isSelectionSoldOut)

        result = uiFactory.setSelectedColour(details = result, index = 1)
        assertFalse(result.isSelectionSoldOut)
    }
}
