package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.wishlist.WishlistUIFactory
import com.mindera.alfie.feature.wishlist.models.WishlistProductUi
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WishlistUIFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: WishlistUIFactory

    @Test
    fun `map wishlist to ui xMedium`() = runTest {
        val result = uiFactory(products) { }

        assertEquals(
            wishListProductUi.map {
                WishlistProductUi(
                    productCardData = ProductCardType.Medium(
                        image = it.productCardData.image,
                        brand = it.productCardData.brand,
                        name = it.productCardData.name,
                        price = it.productCardData.price,
                        color = (it.productCardData as ProductCardType.Medium).color,
                        size = (it.productCardData as ProductCardType.Medium).size
                    )
                )
            },
            result.map {
                WishlistProductUi(
                    productCardData = ProductCardType.Medium(
                        image = it.productCardData.image,
                        brand = it.productCardData.brand,
                        name = it.productCardData.name,
                        price = it.productCardData.price,
                        color = (it.productCardData as ProductCardType.Medium).color,
                        size = (it.productCardData as ProductCardType.Medium).size
                    )
                )
            }
        )
    }
}