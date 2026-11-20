package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.bag.models.BagLineUi
import com.mindera.alfie.feature.bag.models.BagSummaryUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagLine
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

internal fun aud(amount: Double) = Money(amount = amount, amountFormatted = "$" + amount.toInt(), currencyCode = "AUS")

internal val products = listOf(
    Product(
        id = "123456",
        name = "Product name",
        slug = "123456-product",
        brandName = "Brand",
        descriptionHtml = "",
        defaultVariantId = "variant2",
        images = listOf(image1),
        priceRange = PriceRange(low = aud(100.0), high = aud(200.0)),
        variants = listOf(
            Variant(
                id = "variant1Id",
                sku = "variant1",
                price = Price(amount = aud(100.0), was = aud(200.0)),
                options = listOf(
                    VariantOption(name = "color", value = "blue"),
                    VariantOption(name = "size", value = "M")
                ),
                media = listOf(variant1Image),
                availableQuantity = 1
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
        priceRange = PriceRange(low = aud(100.0), high = null),
        variants = listOf(
            Variant(
                id = "variant11Id",
                sku = "variant11",
                price = Price(amount = aud(100.0), was = null),
                options = listOf(
                    VariantOption(name = "color", value = "blue"),
                    VariantOption(name = "size", value = "M")
                ),
                media = listOf(variant1Image),
                availableQuantity = 10
            )
        )
    )
)

internal val productsBySlug = products.associateBy { it.slug }

/** The Cart's view of the two products above: one unit of each. */
internal val bagLines = listOf(
    BagLine(
        id = "line-1",
        productId = "123456",
        variantId = "variant1Id",
        sku = "variant1",
        slug = "123456-product",
        name = "Cart name 1",
        image = null,
        quantity = 1,
        unitPrice = aud(100.0),
        lineTotal = aud(100.0)
    ),
    BagLine(
        id = "line-2",
        productId = "654321",
        variantId = "variant11Id",
        sku = "variant11",
        slug = "654321-product",
        name = "Cart name 2",
        image = null,
        quantity = 1,
        unitPrice = aud(100.0),
        lineTotal = aud(100.0)
    )
)

internal val bag = Bag(lines = bagLines, subtotal = aud(200.0))

internal val bagLineUi = persistentListOf(
    BagLineUi(
        bagLine = bagLines[0],
        notice = BagItemNotice.LowStock(remaining = 1),
        productCardData = ProductCardType.Horizontal(
            brand = "Brand",
            name = "Product name",
            price = PriceType.Sale(fullPrice = "$200", salePrice = "$100"),
            image = variant1Image.toImageUI(),
            color = "blue",
            size = "M",
            reference = "variant1",
            quantity = 1,
            isAvailable = true
        )
    ),
    BagLineUi(
        bagLine = bagLines[1],
        notice = null,
        productCardData = ProductCardType.Horizontal(
            brand = "Brand",
            name = "Product 2",
            price = PriceType.Default(price = "$100"),
            image = variant1Image.toImageUI(),
            color = "blue",
            size = "M",
            reference = "variant11",
            quantity = 1,
            isAvailable = true
        )
    )
)

internal val bagContentUi = BagContentUi(
    lines = bagLineUi,
    summary = BagSummaryUi(totalFormatted = "$200")
)
