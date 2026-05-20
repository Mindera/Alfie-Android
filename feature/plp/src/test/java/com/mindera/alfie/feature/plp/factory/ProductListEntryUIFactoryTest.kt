package com.mindera.alfie.feature.plp.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.plp.products
import com.mindera.alfie.feature.plp.productsVerticalUI
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
            val expected = productsVerticalUI[index]

            val result = uiFactory(
                entry = product,
                onFavoriteClick = { },
                onProductClick = { }
            )

            assertEquals(expected.id, result.id)
            assertEquals(expected.productCardData.brand, result.productCardData.brand)
            assertEquals(expected.productCardData.name, result.productCardData.name)
            assertEquals(expected.productCardData.price, result.productCardData.price)
            assertEquals(expected.productCardData.image, result.productCardData.image)
        }
    }

    @Test
    fun `invoke - WHEN product THEN map to vertical product cards`() = runTest {
        products.forEach { product ->
            val result = uiFactory(
                entry = product,
                onFavoriteClick = { },
                onProductClick = { }
            )

            assertIs<ProductCardType.Vertical>(result.productCardData)
        }
    }

    @Test
    fun `invoke - WHEN onProductClick provided THEN product card onClick is wired to call it`() = runTest {
        products.forEachIndexed { index, product ->
            var invoked = false

            val result = uiFactory(
                entry = product,
                onFavoriteClick = { },
                onProductClick = { invoked = true }
            )

            val onClick = result.productCardData.onClick
            assertNotNull(onClick, "Expected onClick to be set for product at index $index")
            onClick()

            assertTrue(invoked, "Expected onProductClick to be invoked via ProductCardType.Vertical.onClick for product at index $index")
        }
    }
}
