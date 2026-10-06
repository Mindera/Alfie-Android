package com.mindera.alfie.feature.mappers

import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MapImageTest {

    private val productImage = Media.Image(url = "https://example.com/product.jpg", alt = "product")
    private val variantImage = Media.Image(url = "https://example.com/variant.jpg", alt = "variant")

    private fun variant(media: List<Media.Image>) = Variant(
        id = "variant",
        sku = "sku",
        price = Price(amount = Money(amount = 1.0, amountFormatted = "£1.00", currencyCode = "GBP"), was = null),
        options = emptyList(),
        media = media,
        availableQuantity = 1
    )

    private fun product(images: List<Media.Image>) = Product(
        id = "id",
        name = "name",
        slug = "slug",
        brandName = null,
        descriptionHtml = null,
        defaultVariantId = null,
        images = images,
        priceRange = null,
        variants = emptyList()
    )

    @Test
    fun `cardImageFor - WHEN the variant has media THEN its first image is used`() {
        assertEquals(
            variantImage.toImageUI(),
            product(images = listOf(productImage)).cardImageFor(variant(media = listOf(variantImage)))
        )
    }

    @Test
    fun `cardImageFor - WHEN the variant has no media THEN the product's first image is used`() {
        assertEquals(
            productImage.toImageUI(),
            product(images = listOf(productImage)).cardImageFor(variant(media = emptyList()))
        )
    }

    @Test
    fun `cardImageFor - WHEN there is no variant THEN the product's first image is used`() {
        assertEquals(
            productImage.toImageUI(),
            product(images = listOf(productImage)).cardImageFor(variant = null)
        )
    }
}
