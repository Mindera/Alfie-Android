package com.mindera.alfie.feature.pdp

import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonProperties
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.VariantOption
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money

internal const val BASE_URL = "https://www.alfie.com"

private val price = Price(
    amount = Money(currencyCode = "AUD", amount = 400.0, amountFormatted = "$400.00"),
    was = null
)

private val image1 = Media.Image(
    alt = "patterson mini skirt",
    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
)
private val image2 = Media.Image(
    alt = "patterson mini skirt",
    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
)

private fun variant(
    id: String,
    sku: String,
    color: String,
    size: String,
    available: Boolean = true
) = Variant(
    id = id,
    sku = sku,
    price = price,
    options = listOf(
        VariantOption(name = "color", value = color),
        VariantOption(name = "size", value = size)
    ),
    media = listOf(image1, image2),
    available = available
)

internal val product = Product(
    id = "2666503",
    name = "Seamless sculpt mid thigh short",
    slug = "camilla-and-marc-patterson-mini-skirt-26531650",
    brandName = "Camilla and Marc",
    descriptionHtml = "Long description",
    defaultVariantId = "v1",
    images = listOf(image1, image2),
    priceRange = null,
    variants = listOf(
        variant(id = "v1", sku = "sku-steel-10", color = "steel", size = "10 AU"),
        variant(id = "v2", sku = "sku-bone-11", color = "bone", size = "11 AU", available = false),
        variant(id = "v3", sku = "sku-bone-12", color = "bone", size = "12 AU"),
        variant(id = "v4", sku = "sku-steel-6", color = "steel", size = "6 AU")
    )
)

internal val sizeUI = SizeUI(
    id = "10 AU",
    properties = SizingButtonProperties(
        text = "10 AU",
        state = SizingButtonState.Selectable
    )
)
