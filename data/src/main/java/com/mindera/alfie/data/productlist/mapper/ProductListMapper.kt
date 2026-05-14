package com.mindera.alfie.data.productlist.mapper

import com.mindera.alfie.data.shared.mapper.toDomain
import com.mindera.alfie.data.toDomain
import com.mindera.alfie.graphql.ProductListingQuery
import com.mindera.alfie.graphql.fragment.PaginationInfo
import com.mindera.alfie.graphql.fragment.ProductInfo
import com.mindera.alfie.graphql.fragment.VariantInfo
import com.mindera.alfie.repository.product.model.Color
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListEntryVariant
import com.mindera.alfie.repository.shared.model.Pagination

internal fun ProductListingQuery.ProductListing.toDomain() = ProductList(
    title = title,
    products = products.map { it.productInfo.toDomain() },
    pagination = pagination.paginationInfo.toDomain()
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
