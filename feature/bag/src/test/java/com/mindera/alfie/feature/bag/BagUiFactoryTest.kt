package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagProductUi
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
class BagUiFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: BagUiFactory

    @Test
    fun `invoke - map to expected Bag UI`() = runTest {
        val items = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onRemoveClick = { },
            onProductClick = { }
        )
        val expected = bagProductUi.map {
            val productCard = it.productCardData as ProductCardType.Horizontal
            BagProductUi(
                id = it.id,
                productCardData = ProductCardType.Horizontal(
                    image = it.productCardData.image,
                    brand = it.productCardData.brand,
                    name = it.productCardData.name,
                    price = it.productCardData.price,
                    color = productCard.color,
                    size = productCard.size
                )
            )
        }
        val result = items.map {
            val productCard = it.productCardData as ProductCardType.Horizontal
            BagProductUi(
                id = it.id,
                productCardData = ProductCardType.Horizontal(
                    image = it.productCardData.image,
                    brand = it.productCardData.brand,
                    name = it.productCardData.name,
                    price = it.productCardData.price,
                    color = productCard.color,
                    size = productCard.size
                )
            )
        }

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN onProductClick provided THEN each product card onClick is wired to call it with product id`() = runTest {
        val clickedIds = mutableListOf<String>()

        val items = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onRemoveClick = { },
            onProductClick = { id -> clickedIds.add(id) }
        )

        items.forEachIndexed { index, bagProductUi ->
            val onClick = (bagProductUi.productCardData as ProductCardType.Horizontal).onClick
            assertNotNull(onClick, "Expected onClick to be set for item at index $index")
            onClick()
        }

        val expectedIds = bagProducts.map { it.productId }
        assertEquals(expectedIds, clickedIds)
    }
}