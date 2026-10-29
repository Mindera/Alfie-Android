package com.mindera.alfie.repository.productlist.model

/**
 * Identifies which BFF query backs a product list.
 *
 * - [Collection] resolves to the `productList(collectionHandle, ...)` query.
 * - [Search] resolves to the `searchProducts(searchTerm, ...)` query.
 * - [Brand] resolves to `searchProducts` for the brand's name, narrowed to the brand by
 *   `filters.brandNames`. The BFF has no catalogue-wide listing, and a blank search term returns
 *   nothing, so the name is the term.
 *
 * All return the same `ProductListResponse` shape, so pagination, sort and filter
 * plumbing is shared regardless of the source.
 */
sealed interface ProductListQuerySource {

    data class Collection(val handle: String) : ProductListQuerySource

    data class Search(val term: String) : ProductListQuerySource

    data class Brand(
        val key: String,
        val name: String
    ) : ProductListQuerySource
}
