package com.mindera.alfie.feature.pdp

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Runs under Robolectric because [ProductDetailsUIFactory.invoke] maps the product description via
 * `stripHtml` -> `HtmlCompat.fromHtml` (android.text.Html), which requires the Android framework.
 * JUnit4 here is executed on the JUnit5 Platform via the junit-vintage engine.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ProductDetailsUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @RelaxedMockK
    private lateinit var environmentManager: EnvironmentManager

    @InjectMockKs
    private lateinit var uiFactory: ProductDetailsUIFactory

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { dispatcherProvider.default() } returns Dispatchers.Main
        coEvery { environmentManager.current() } returns Environment.Prod(graphQLUrl = "", webUrl = BASE_URL)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

    @Test
    fun `resolveDefaultVariant - WHEN defaultVariantId matches a variant THEN that variant's colour is selected`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "v3",
            variants = listOf(
                variant(id = "v1", sku = "s1", color = "steel", size = "10 AU"),
                variant(id = "v3", sku = "s3", color = "bone", size = "12 AU")
            )
        )

        val result = uiFactory(testProduct)

        assertEquals("bone", result.selectedColorUI?.id)
    }

    @Test
    fun `resolveDefaultVariant - WHEN defaultVariantId is absent THEN falls back to first available variant`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "missing",
            variants = listOf(
                variant(id = "a", sku = "sa", color = "red", size = "S", available = false),
                variant(id = "b", sku = "sb", color = "blue", size = "M", available = true)
            )
        )

        val result = uiFactory(testProduct)

        assertEquals("blue", result.selectedColorUI?.id)
    }

    @Test
    fun `resolveDefaultVariant - WHEN no variant is available THEN falls back to the first variant`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "missing",
            variants = listOf(
                variant(id = "a", sku = "sa", color = "red", size = "S", available = false),
                variant(id = "b", sku = "sb", color = "blue", size = "M", available = false)
            )
        )

        val result = uiFactory(testProduct)

        assertEquals("red", result.selectedColorUI?.id)
    }

    @Test
    fun `resolveDefaultVariant - WHEN defaultVariantId is null THEN falls back to first available variant`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = null,
            variants = listOf(
                variant(id = "a", sku = "sa", color = "red", size = "S", available = false),
                variant(id = "b", sku = "sb", color = "blue", size = "M", available = true)
            )
        )

        val result = uiFactory(testProduct)

        assertEquals("blue", result.selectedColorUI?.id)
    }

    @Test
    fun `resolveDefaultVariant - WHEN variants are empty THEN does not crash and selects no colour`() = runTest {
        val testProduct = product.copy(defaultVariantId = null, variants = emptyList())

        val result = uiFactory(testProduct)

        assertTrue(result.colors.isEmpty())
        assertNull(result.selectedColorUI)
    }

    @Test
    fun `setSelectedColour - WHEN the selected colour has no media THEN retains the existing gallery`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "v1",
            variants = listOf(
                variant(id = "v1", sku = "s1", color = "steel", size = "10 AU"),
                variant(id = "v2", sku = "s2", color = "bone", size = "12 AU").copy(media = emptyList())
            )
        )
        val details = uiFactory(testProduct)
        val galleryBeforeSwitch = details.gallery

        // index 1 = "bone", whose variant carries no media — gallery must not go blank.
        val result = uiFactory.setSelectedColour(details = details, index = 1)

        assertEquals("bone", result.selectedColorUI?.id)
        assertEquals(galleryBeforeSwitch, result.gallery)
    }
}
