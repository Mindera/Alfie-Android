package au.com.alfie.ecomm.data.product.mapper

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.ProductQuery
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant

internal fun ProductQuery.Product.toDomain() = Product(
    id = id,
    attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
    brand = brand.brandInfo.toDomain(),
    defaultVariant = defaultVariant.variantInfo.toDomain(),
    hierarchy = hierarchy.map { it.hierarchyItemsTree.hierarchyItemInfo.toDomain() },
    labels = labels.orEmpty(),
    longDescription = longDescription,
    name = name,
    priceRange = priceRange?.priceRangeInfo?.toDomain(),
    shortDescription = shortDescription,
    sizes = sizes?.map { it.sizeContainer.toDomain() }.orEmpty(),
    slug = slug,
    styleNumber = styleNumber,
    variants = variants.map { it.variantInfo.toDomain() }
)

internal fun ColorInfo.toDomain() = Color(
    id = id,
    name = name,
    swatch = swatch?.imageInfo?.toDomain()
)

private fun VariantInfo.toDomain() = Variant(
    attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
    color = colour?.colorInfo?.toDomain(),
    media = media.mapNotNull { it.mediaInfo.onImage?.imageInfo?.toDomain() ?: it.mediaInfo.onVideo?.videoInfo?.toDomain() },
    price = price.priceInfo.toDomain(),
    size = size?.sizeContainer?.toDomain(),
    sku = sku,
    stock = stock
)
