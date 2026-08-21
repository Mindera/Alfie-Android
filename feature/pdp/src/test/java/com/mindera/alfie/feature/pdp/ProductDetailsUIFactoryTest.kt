package com.mindera.alfie.feature.pdp

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonProperties
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.VariantOption
import com.mindera.alfie.repository.shared.model.Money
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

    @Test
    fun `toSizeSectionUI - WHEN a third option duplicates a size THEN it collapses to one chip`() = runTest {
        // Live BFF shape: "Sleeve length type" splits one colour+size into sibling variants.
        val sleeveVariant = { id: String, sku: String, available: Boolean ->
            variant(id = id, sku = sku, color = "steel", size = "10 AU", available = available)
                .copy(options = listOf(VariantOption("color", "steel"), VariantOption("size", "10 AU"), VariantOption("Sleeve length type", "Long")))
        }
        val testProduct = product.copy(
            defaultVariantId = "v1",
            variants = listOf(
                sleeveVariant("v1", "sku-long", false),
                sleeveVariant("v2", "sku-short", true),
                variant(id = "v3", sku = "sku-6", color = "steel", size = "6 AU")
            )
        )

        val result = uiFactory(testProduct)

        val sizeSelector = result.sizeSectionUI as SizeSectionUI.SizeSelector
        // "10 AU" appears twice (Long/Sleeveless) but must collapse to a single chip.
        assertEquals(2, sizeSelector.sizes.size)
        assertEquals(1, sizeSelector.sizes.count { it.id == "10 AU" })
        // The in-stock sibling wins the dedupe, so the chip stays selectable.
        assertEquals(SizingButtonState.Selectable, sizeSelector.sizes.first { it.id == "10 AU" }.properties.state)
    }

    @Test
    fun `getSelectedVariantSku - WHEN sibling variants share colour and size THEN prefers the available one`() = runTest {
        val sleeveVariant = { id: String, sku: String, available: Boolean ->
            variant(id = id, sku = sku, color = "steel", size = "10 AU", available = available)
                .copy(options = listOf(VariantOption("color", "steel"), VariantOption("size", "10 AU"), VariantOption("Sleeve length type", "Long")))
        }
        val testProduct = product.copy(
            defaultVariantId = "v1",
            variants = listOf(
                sleeveVariant("v1", "sku-long", false),
                sleeveVariant("v2", "sku-short", true),
                variant(id = "v3", sku = "sku-6", color = "steel", size = "6 AU")
            )
        )
        val details = uiFactory(testProduct)
        val withSize = uiFactory.setSelectedSize(details = details, sizeUI = sizeUI)

        val sku = uiFactory.getSelectedVariantSku(withSize)

        assertEquals("sku-short", sku)
    }

    @Test
    fun `invoke - WHEN compareAtPrice is above the price THEN renders a sale price`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "v1",
            variants = listOf(
                variant(id = "v1", sku = "s1", color = "steel", size = "10 AU").copy(
                    price = Price(
                        amount = Money(currencyCode = "GBP", amount = 8.0, amountFormatted = "£8.00"),
                        was = Money(currencyCode = "GBP", amount = 10.0, amountFormatted = "£10.00")
                    )
                ),
                variant(id = "v2", sku = "s2", color = "bone", size = "12 AU")
            )
        )

        val result = uiFactory(testProduct)

        assertEquals(PriceType.Sale(fullPrice = "£10.00", salePrice = "£8.00"), result.price)
    }

    @Test
    fun `invoke - WHEN there is no was-price THEN renders a default price`() = runTest {
        val result = uiFactory(product)

        assertEquals(PriceType.Default("$400.00"), result.price)
    }

    @Test
    fun `invoke - exposes the selected colour name and the display variant reference`() = runTest {
        val result = uiFactory(product)

        assertEquals("steel", result.selectedColourName)
        // defaultVariantId "v1" is steel/10 AU — the display variant until a size is picked.
        assertEquals("sku-steel-10", result.productReference)
    }

    @Test
    fun `setSelectedSize - recomputes the price from the exact selected variant`() = runTest {
        val testProduct = product.copy(
            defaultVariantId = "v1",
            variants = listOf(
                variant(id = "v1", sku = "s1", color = "steel", size = "10 AU"),
                variant(id = "v2", sku = "s2", color = "steel", size = "6 AU").copy(
                    price = Price(
                        amount = Money(currencyCode = "GBP", amount = 12.0, amountFormatted = "£12.00"),
                        was = Money(currencyCode = "GBP", amount = 15.0, amountFormatted = "£15.00")
                    )
                )
            )
        )
        val details = uiFactory(testProduct)
        val sixAu = SizeUI(id = "6 AU", properties = SizingButtonProperties(text = "6 AU", state = SizingButtonState.Selectable))

        val result = uiFactory.setSelectedSize(details = details, sizeUI = sixAu)

        assertEquals(PriceType.Sale(fullPrice = "£15.00", salePrice = "£12.00"), result.price)
        assertEquals("s2", result.productReference)
    }
}
