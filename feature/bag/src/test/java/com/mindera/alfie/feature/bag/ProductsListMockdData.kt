package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.VariantOption
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import kotlinx.collections.immutable.persistentListOf

private val image1 = Media.Image(
    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
    alt = "patterson mini skirt"
)

private val variant1Image = Media.Image(url = "", alt = "Media")

internal val products = listOf(
    Product(
        id = "123456",
        name = "Product name",
        slug = "123456-product",
        brandName = "Brand",
        descriptionHtml = "",
        defaultVariantId = "variant2",
        images = listOf(image1),
        priceRange = PriceRange(
            low = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
            high = Money(amount = 200, amountFormatted = "$200", currencyCode = "AUS")
        ),
        variants = listOf(
            Variant(
                id = "variant1Id",
                sku = "variant1",
                price = Price(
                    amount = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
                    was = Money(amount = 200, amountFormatted = "$200", currencyCode = "AUS")
                ),
                options = listOf(
                    VariantOption(name = "color", value = "blue"),
                    VariantOption(name = "size", value = "M")
                ),
                media = listOf(variant1Image),
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
                id = "variant11Id",
                sku = "variant11",
                price = Price(
                    amount = Money(amount = 100, amountFormatted = "$100", currencyCode = "AUS"),
                    was = null
                ),
                options = listOf(
                    VariantOption(name = "color", value = "blue"),
                    VariantOption(name = "size", value = "M")
                ),
                media = listOf(variant1Image),
                available = true
            )
        )
    )
)

internal val bagProducts = listOf(
    BagProduct(productId = "123456", variantSku = "variant1"),
    BagProduct(productId = "654321", variantSku = "variant11")
)

internal val bagProductUi = persistentListOf(
    BagProductUi(
        id = "123456",
        productCardData = ProductCardType.Horizontal(
            brand = "Brand",
            name = "Product name",
            price = PriceType.Range(startPrice = "$100", endPrice = "$200"),
            image = variant1Image.toImageUI(),
            color = "blue",
            size = "M"
        )
    ),
    BagProductUi(
        id = "654321",
        productCardData = ProductCardType.Horizontal(
            brand = "Brand",
            name = "Product 2",
            price = PriceType.Default(price = "$100"),
            image = variant1Image.toImageUI(),
            color = "blue",
            size = "M"
        )
    )
)
