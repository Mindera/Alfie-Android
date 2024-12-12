package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.plp.products
import au.com.alfie.ecomm.feature.plp.productsMediumUI
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class ProductListEntryUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: ProductListEntryUIFactory

    @BeforeEach
    fun setUp() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - correctly maps a product entry`() = runTest {
        products.forEachIndexed { index, product ->
            val expected = productsMediumUI[index]

            val result = uiFactory(
                entry = product,
                layoutMode = ProductListLayoutMode.GRID,
                onFavoriteClick = { }
            )

            assertEquals(expected.id, result.id)
            assertEquals(expected.productCardData.brand, result.productCardData.brand)
            assertEquals(expected.productCardData.name, result.productCardData.name)
            assertEquals(expected.productCardData.price, result.productCardData.price)
            assertEquals(expected.productCardData.image, result.productCardData.image)
        }
    }

    @Test
    fun `invoke - WHEN layout mode is GRID THEN map to medium product cards`() = runTest {
        products.forEach { product ->
            val result = uiFactory(
                entry = product,
                layoutMode = ProductListLayoutMode.GRID,
                onFavoriteClick = { }
            )

            assertIs<ProductCardType.Medium>(result.productCardData)
        }
    }

    @Test
    fun `invoke - WHEN layout mode is COLUMN THEN map to large product cards`() = runTest {
        products.forEach { product ->
            val result = uiFactory(
                entry = product,
                layoutMode = ProductListLayoutMode.COLUMN,
                onFavoriteClick = { }
            )

            assertIs<ProductCardType.Large>(result.productCardData)
        }
    }
}
