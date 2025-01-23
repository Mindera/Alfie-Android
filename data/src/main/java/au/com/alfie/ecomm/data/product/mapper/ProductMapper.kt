package au.com.alfie.ecomm.data.product.mapper

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.data.toDomain
import au.com.alfie.ecomm.graphql.fragment.ProductInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Media

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
    color = colors.first { it.id == colour?.id },
    media = colors.first { it.id == colour?.id }.media?.first{ it is Media.Image } as Media.Image,
    price = price.priceInfo.toDomain(),
    size = size?.sizeContainer?.toDomain(),
    sku = sku,
    stock = stock,
)
