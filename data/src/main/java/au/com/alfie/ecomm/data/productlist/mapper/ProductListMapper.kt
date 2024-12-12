package au.com.alfie.ecomm.data.productlist.mapper

import au.com.alfie.ecomm.data.product.mapper.toDomain
import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.ProductListingQuery
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntryVariant
import au.com.alfie.ecomm.repository.shared.model.Pagination

internal fun ProductListingQuery.ProductListing.toDomain() = ProductList(
    title = title,
    products = products.map { it.toDomain() },
    pagination = pagination.toDomain(),
    hierarchy = hierarchy?.map { it.hierarchyItemsTree.hierarchyItemInfo.toDomain() }.orEmpty()
)

private fun ProductListingQuery.Pagination.toDomain() = Pagination(
    limit = limit,
    offset = offset,
    page = page,
    pageCount = pages,
    total = total,
    nextPage = nextPage,
    previousPage = previousPage
)

private fun ProductListingQuery.Product.toDomain() = ProductListEntry(
    id = id,
    name = name,
    shortDescription = shortDescription,
    slug = slug,
    styleNumber = styleNumber,
    labels = labels.orEmpty(),
    brand = brand.brandInfo.toDomain(),
    colors = colours?.map { it.colorInfo.toDomain() }.orEmpty(),
    priceRange = priceRange?.priceRangeInfo?.toDomain(),
    sizes = sizes?.map { it.sizeContainer.toDomain() }.orEmpty(),
    defaultVariant = defaultVariant.toDomain()
)

private fun ProductListingQuery.DefaultVariant.toDomain() = ProductListEntryVariant(
    color = colour?.colorInfo?.toDomain(),
    size = size?.sizeContainer?.toDomain(),
    price = price.priceInfo.toDomain(),
    stock = stock,
    media = media.mapNotNull { it.onImage?.imageInfo?.toDomain() }
)
