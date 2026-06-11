package com.mindera.alfie.feature.plp

import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource

/**
 * The BFF query source backing a [ProductListType].
 *
 * [ProductListType.Search] threads its term straight through to `searchProducts`. Every other
 * type currently falls back to [collectionHandle] because the BFF does not yet expose a query to
 * resolve a collection handle from a category/brand slug or id — when it does, this is the single
 * place that mapping needs to change.
 */
internal fun ProductListType.toQuerySource(collectionHandle: String): ProductListQuerySource =
    when (this) {
        is ProductListType.Search -> ProductListQuerySource.Search(term = query)
        else -> ProductListQuerySource.Collection(handle = collectionHandle)
    }

/**
 * Human-readable title for a [ProductListType], shown in the top bar.
 *
 * Note: for non-search lists this may not match the products shown while the collection handle is
 * still hardcoded (see [toQuerySource]).
 */
internal val ProductListType.displayTitle: String
    get() = when (this) {
        is ProductListType.Category.Slug -> slug
        is ProductListType.Category.Id -> id
        is ProductListType.Brand.Slug -> slug
        is ProductListType.Brand.Id -> id
        is ProductListType.Search -> query
    }
