package au.com.alfie.ecomm.data.product.mapper

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.ProductQuery
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Media

internal fun ProductQuery.Product.toDomain(): Product {
    val colors = colours?.map { it.colorInfo.toDomain() }.orEmpty()
    return Product(
        id = id,
        attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
        brand = brand.brandInfo.toDomain(),
        defaultVariant = defaultVariant.variantInfo.toDomain(colors),
        hierarchy = hierarchy.map { it.hierarchyItemsTree.hierarchyItemInfo.toDomain() },
        labels = labels.orEmpty(),
        longDescription = longDescription,
        name = name,
        priceRange = priceRange?.priceRangeInfo?.toDomain(),
        shortDescription = shortDescription,
        sizes = sizes?.map { it.sizeContainer.toDomain() }.orEmpty(),
        slug = slug,
        styleNumber = styleNumber,
        variants = variants.map { it.variantInfo.toDomain(colors) },
        colors = colors
    )
}

internal fun ColorInfo.toDomain() = Color(
    id = id,
    name = name,
    swatch = swatch?.imageInfo?.toDomain(),
    media = media?.map { it?.mediaInfo?.toDomain() }
)

private fun VariantInfo.toDomain(colors: List<Color>) = Variant(
    attributes = attributes?.map { it.attributesInfo.toDomain() }.orEmpty(),
    color = colors.first { it.id == colour?.id },
    media = colors.first { it.id == colour?.id }.media?.first{ it is Media.Image } as Media.Image,
    price = price.priceInfo.toDomain(),
    size = size?.sizeContainer?.toDomain(),
    sku = sku,
    stock = stock,
)
