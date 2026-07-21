package com.mindera.alfie.feature.plp

import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource

/**
 * Maps each [ProductListType] to the BFF query source that backs the list.
 *
 * [ProductListType.Search] threads its term straight through to `searchProducts`. Every other
 * type uses its own slug/id directly as the collection handle for the `productList` query.
 */
internal fun ProductListType.toQuerySource(): ProductListQuerySource =
    when (this) {
        is ProductListType.Search -> ProductListQuerySource.Search(term = query)
        is ProductListType.Category.Slug -> ProductListQuerySource.Collection(handle = slug)
        is ProductListType.Category.Id -> ProductListQuerySource.Collection(handle = id)
        is ProductListType.Brand.Slug -> ProductListQuerySource.Collection(handle = slug)
        is ProductListType.Brand.Id -> ProductListQuerySource.Collection(handle = id)
    }

/**
 * Human-readable title for a [ProductListType], shown in the top bar.
 */
internal val ProductListType.displayTitle: String
    get() = when (this) {
        is ProductListType.Category.Slug -> slug
        is ProductListType.Category.Id -> id
        is ProductListType.Brand.Slug -> slug
        is ProductListType.Brand.Id -> id
        is ProductListType.Search -> query
    }
