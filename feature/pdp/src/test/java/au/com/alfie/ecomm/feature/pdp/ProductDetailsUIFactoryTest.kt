package au.com.alfie.ecomm.feature.pdp

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.feature.pdp.model.SizeSectionUI
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
    fun `invoke - returns the product details`() = runTest {
        val result = uiFactory(product)
        val expected = productDetailsUI

        assertEquals(expected, result)
    }

    @Test
    fun `setSelectedColour - returns updated product details`() = runTest {
        val result = uiFactory(product)
        val updatedResult = uiFactory.setSelectedColour(
            details = result,
            index = 1
        )
        val expected = productDetailsUI.copy(
            selectedColorUI = result.colors[1],
            sizeSectionUI = newColorSizeSectionUI,
            gallery = selectedColorGalleryUI
        )

        assertEquals(expected, updatedResult)
    }

    @Test
    fun `setSelectedSize - returns updated product details`() = runTest {
        val result = uiFactory(product)
        val updatedResult = uiFactory.setSelectedSize(
            details = result,
            sizeUI = sizeUI
        )
        val expected = productDetailsUI.copy(
            sizeSectionUI = (result.sizeSectionUI as SizeSectionUI.SizeSelector).copy(selectedSize = sizeUI)
        )

        assertEquals(expected, updatedResult)
    }

    @Test
    fun `invoke - WHEN selection is out of stock THEN bag button is not enabled`() = runTest {
        val updatedStockVariants = product.variants.map { entry ->
            entry.copy(stock = if (entry.color?.id == product.defaultVariant.color?.id) 0 else 100)
        }
        val updatedProduct = product.copy(variants = updatedStockVariants)
        var result = uiFactory(updatedProduct)

        assertTrue(result.isSelectionSoldOut)

        result = uiFactory.setSelectedColour(
            details = result,
            index = 1
        )
        assertFalse(result.isSelectionSoldOut)
    }
}
