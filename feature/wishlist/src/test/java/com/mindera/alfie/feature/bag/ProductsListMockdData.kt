package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.wishlist.models.WishlistProductUi
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.VariantOption
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import kotlinx.collections.immutable.persistentListOf

private val productImage = Media.Image(
    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
    alt = "patterson mini skirt"
)
private val variantImage = Media.Image(url = "", alt = "Media")
private val variantImageNoAlt = Media.Image(url = "", alt = null)

internal val products = listOf(
    Product(
        id = "123456",
        name = "Product name",
        slug = "123456-product",
        brandName = "Brand",
        descriptionHtml = "",
        defaultVariantId = null,
        images = listOf(productImage),
        priceRange = PriceRange(
            low = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
            high = Money(amount = 200, amountFormatted = "$200", currencyCode = "AUS")
        ),
        variants = listOf(
            Variant(
                id = "variantId",
                sku = "234rtghnm",
                price = Price(
                    amount = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
                    was = Money(amount = 200, amountFormatted = "$200", currencyCode = "AUS")
                ),
                options = listOf(
                    VariantOption(name = "color", value = "blue"),
                    VariantOption(name = "size", value = "M")
                ),
                media = listOf(variantImage),
                available = true
            )
        )
    ),
    Product(
        id = "654321",
        name = "Product 2",
        slug = "654321-product",
        brandName = "Brand",
        descriptionHtml = "",
        defaultVariantId = null,
        images = emptyList(),
        priceRange = PriceRange(
            low = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
            high = null
        ),
        variants = listOf(
            Variant(
                id = "variantId2",
                sku = "234rtghnm",
                price = Price(
                    amount = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
                    was = null
                ),
                options = emptyList(),
                media = listOf(variantImageNoAlt),
                available = true
            )
        )
    )
)

internal val wishListProductUi = persistentListOf(
    WishlistProductUi(
        productCardData = ProductCardType.Vertical(
            brand = "Brand",
            name = "Product name",
            price = PriceType.Range(startPrice = "$100", endPrice = "$200"),
            image = variantImage.toImageUI()
        )
    ),
    WishlistProductUi(
        productCardData = ProductCardType.Vertical(
            brand = "Brand",
            name = "Product 2",
            price = PriceType.Default("$100"),
            image = variantImageNoAlt.toImageUI()
        )
    )
)
