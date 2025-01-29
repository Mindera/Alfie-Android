package au.com.alfie.ecomm.data.productlist.mapper

import au.com.alfie.ecomm.data.toDomain
import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.ProductListingQuery
import au.com.alfie.ecomm.graphql.fragment.PaginationInfo
import au.com.alfie.ecomm.graphql.fragment.ProductInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntryVariant
import au.com.alfie.ecomm.repository.shared.model.Pagination

internal fun ProductListingQuery.ProductListing.toDomain() = ProductList(
    title = title,
    products = products.map { it.productInfo.toDomain() },
    pagination = pagination.paginationInfo.toDomain(),
)

private fun PaginationInfo.toDomain() = Pagination(
    limit = limit,
    offset = offset,
    page = page,
    pageCount = pages,
    total = total,
    nextPage = nextPage,
    previousPage = previousPage
)

private fun ProductInfo.toDomain(): ProductListEntry {
    val colors = colours?.map { it.colorInfo.toDomain() }.orEmpty()
    return ProductListEntry(
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

private fun VariantInfo.toDomain(colors: List<Color>) = ProductListEntryVariant(
    color = colour?.id,
    size = size?.sizeContainer?.toDomain(),
    price = price.priceInfo.toDomain(),
    stock = stock,
    media = colors.first { it.id == colour?.id }.media
)
