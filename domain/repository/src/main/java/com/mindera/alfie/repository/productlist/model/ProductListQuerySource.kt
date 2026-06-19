package com.mindera.alfie.repository.productlist.model

/**
 * Identifies which BFF query backs a product list.
 *
 * - [Collection] resolves to the `productList(collectionHandle, ...)` query.
 * - [Search] resolves to the `searchProducts(searchTerm, ...)` query.
 *
 * Both return the same `ProductListResponse` shape, so pagination, sort and filter
 * plumbing is shared regardless of the source.
 */
sealed interface ProductListQuerySource {

    data class Collection(val handle: String) : ProductListQuerySource

    data class Search(val term: String) : ProductListQuerySource
}
