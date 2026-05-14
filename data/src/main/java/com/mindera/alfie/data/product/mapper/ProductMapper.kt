package com.mindera.alfie.data.product.mapper

import com.mindera.alfie.data.shared.mapper.toDomain
import com.mindera.alfie.data.toDomain
import com.mindera.alfie.graphql.fragment.ProductInfo
import com.mindera.alfie.graphql.fragment.VariantInfo
import com.mindera.alfie.repository.product.model.Color
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.shared.model.Media

internal fun ProductInfo.toDomain(): Product {
    val colors = colours?.map { it.colorInfo.toDomain() }.orEmpty()
    return Product(
        id = id,
        name = name,
        shortDescription = shortDescription,
        slug = slug,
        styleNumber = styleNumber,
        labels = labels.orEmpty(),
        brand = brand.brandInfo.toDomain(),
        colors = colors,
        priceRange = priceRange?.priceRangeInfo?.toDomain(),
        longDescription = longDescription,
        attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
        defaultVariant = defaultVariant.variantInfo.toDomain(colors),
        variants = variants.map { it.variantInfo.toDomain(colors) }
    )
}

private fun VariantInfo.toDomain(colors: List<Color>) = Variant(
    attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
    color = colors.find { it.id == colour?.id },
    media = colors.firstOrNull { it.id == colour?.id }?.media?.first { it is Media.Image } as Media.Image,
    price = price.priceInfo.toDomain(),
    size = size?.sizeContainer?.toDomain(),
    sku = sku,
    stock = stock
)
