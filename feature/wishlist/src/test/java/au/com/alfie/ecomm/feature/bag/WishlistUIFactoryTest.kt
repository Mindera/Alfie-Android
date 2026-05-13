package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.wishlist.WishlistUIFactory
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WishlistUIFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: WishlistUIFactory

    @Test
    fun `WHEN products are mapped THEN product card data contains correct brand, name and price`() = runTest {
        val result = uiFactory(
            products = products,
            onRemoveClick = { },
            onAddToBagClick = { },
            onProductClick = { }
        )

        val expected = wishListProductUi.map {
            val vericalProductType = it.productCardData as ProductCardType.Vertical
            ProductCardType.Vertical(
                image = vericalProductType.image,
                brand = vericalProductType.brand,
                name = vericalProductType.name,
                price = vericalProductType.price
            )
        }
        val actual = result.map {
            val vericalProductType = it.productCardData as ProductCardType.Vertical
            ProductCardType.Vertical(
                image = vericalProductType.image,
                brand = vericalProductType.brand,
                name = vericalProductType.name,
                price = vericalProductType.price
            )
        }

        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN onProductClick is provided THEN onClick on WishlistProductUi is pre-wired`() = runTest {
        var invoked = false

        val result = uiFactory(
            products = products,
            onRemoveClick = { },
            onAddToBagClick = { },
            onProductClick = { invoked = true }
        )

        result.first().onClick()

        assert(invoked) { "Expected onProductClick to be invoked via WishlistProductUi.onClick" }
    }

    @Test
    fun `WHEN onAddToBagClick is provided THEN addToBagClick on ProductCardType is pre-wired`() = runTest {
        var invoked = false

        val result = uiFactory(
            products = products,
            onRemoveClick = { },
            onAddToBagClick = { invoked = true },
            onProductClick = { }
        )

        val vertical = result.first().productCardData as ProductCardType.Vertical
        assertNotNull(vertical.addToBagClick)
        vertical.addToBagClick!!()

        assert(invoked) { "Expected onAddToBagClick to be invoked via ProductCardType.Vertical.addToBagClick" }
    }

    @Test
    fun `WHEN onRemoveClick is provided THEN onRemoveClick on ProductCardType is pre-wired`() = runTest {
        var invoked = false

        val result = uiFactory(
            products = products,
            onRemoveClick = { invoked = true },
            onAddToBagClick = { },
            onProductClick = { }
        )

        val vertical = result.first().productCardData as ProductCardType.Vertical
        assertNotNull(vertical.onRemoveClick)
        vertical.onRemoveClick!!()

        assert(invoked) { "Expected onRemoveClick to be invoked via ProductCardType.Vertical.onRemoveClick" }
    }

    @Test
    fun `WHEN products are mapped THEN result size matches input`() = runTest {
        val result = uiFactory(
            products = products,
            onRemoveClick = { },
            onAddToBagClick = { },
            onProductClick = { }
        )

        assertEquals(products.size, result.size)
    }
}