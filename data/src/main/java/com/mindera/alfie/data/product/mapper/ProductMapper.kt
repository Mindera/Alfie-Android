package com.mindera.alfie.data.product.mapper

import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.graphql.bff.fragment.ImageFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.graphql.bff.fragment.ProductFragment
import com.mindera.alfie.graphql.bff.fragment.ProductVariantFragment
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.VariantOption
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money

internal fun ProductFragment.toDomain(): Product = Product(
    id = id,
    name = name,
    slug = slug,
    brandName = brandName,
    descriptionHtml = descriptionHtml,
    defaultVariantId = defaultVariantId,
    images = images.map { it.imageFragment.toDomain() },
    priceRange = priceRange.toDomain(),
    variants = variants?.mapNotNull { it?.productVariantFragment?.toDomain() }.orEmpty()
)

private fun ProductFragment.PriceRange.toDomain(): PriceRange = PriceRange(
    low = minVariantPrice.moneyFragment.toDomain(),
    high = maxVariantPrice.moneyFragment.toDomain()
)

private fun ProductVariantFragment.toDomain(): Variant = Variant(
    id = id,
    sku = sku,
    price = Price(
        amount = price.moneyFragment.toDomain(),
        was = compareAtPrice?.moneyFragment?.toDomain()
    ),
    options = optionValues.map { VariantOption(name = it.name, value = it.value) },
    media = media?.mapNotNull { it?.imageFragment?.toDomain() }.orEmpty(),
    available = (inventory?.available ?: 0) > 0
)

private fun MoneyFragment.toDomain(): Money = Money(
    currencyCode = currencyCode,
    amount = amount.toInt(),
    amountFormatted = formatMoney(amount = amount.toDouble(), currencyCode = currencyCode)
)

private fun ImageFragment.toDomain(): Media.Image = Media.Image(
    url = url,
    alt = altText
)
