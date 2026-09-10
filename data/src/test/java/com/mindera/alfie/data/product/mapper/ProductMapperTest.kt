package com.mindera.alfie.data.product.mapper

import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.graphql.bff.fragment.ProductFragment
import com.mindera.alfie.graphql.bff.fragment.ProductVariantFragment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ProductMapperTest {

    @Test
    fun `toDomain - keeps the inventory count so the bag can surface low stock`() {
        val product = productFragment(available = 3).toDomain()

        assertEquals(3, product.variants.single().availableQuantity)
        assertTrue(product.variants.single().available)
    }

    @Test
    fun `toDomain - WHEN inventory is zero THEN the variant is unavailable`() {
        val product = productFragment(available = 0).toDomain()

        assertEquals(0, product.variants.single().availableQuantity)
        assertFalse(product.variants.single().available)
    }

    @Test
    fun `toDomain - WHEN inventory is missing THEN the variant is unavailable with no units`() {
        val product = productFragment(available = null).toDomain()

        assertEquals(0, product.variants.single().availableQuantity)
        assertFalse(product.variants.single().available)
    }

    private fun productFragment(available: Int?) = ProductFragment(
        id = "p-1",
        name = "Linen Shirt",
        slug = "linen-shirt",
        brandName = "Alfie",
        descriptionHtml = null,
        defaultVariantId = "v-1",
        primaryImage = null,
        images = emptyList(),
        priceRange = ProductFragment.PriceRange(
            minVariantPrice = ProductFragment.MinVariantPrice(
                __typename = "Money",
                moneyFragment = MoneyFragment(amount = 82.0, currencyCode = "GBP")
            ),
            maxVariantPrice = ProductFragment.MaxVariantPrice(
                __typename = "Money",
                moneyFragment = MoneyFragment(amount = 82.0, currencyCode = "GBP")
            )
        ),
        options = null,
        variants = listOf(
            ProductFragment.Variant(
                __typename = "ProductVariant",
                productVariantFragment = ProductVariantFragment(
                    id = "v-1",
                    sku = "0283/764",
                    price = ProductVariantFragment.Price(
                        __typename = "Money",
                        moneyFragment = MoneyFragment(amount = 82.0, currencyCode = "GBP")
                    ),
                    compareAtPrice = null,
                    optionValues = emptyList(),
                    inventory = ProductVariantFragment.Inventory(available = available),
                    media = emptyList()
                )
            )
        )
    )
}
