package com.mindera.alfie.data.productlist.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.graphql.bff.SearchProductsQuery
import com.mindera.alfie.graphql.bff.type.ProductFilterInput
import com.mindera.alfie.graphql.bff.type.ProductSortEnum
import com.mindera.alfie.network.di.NewClient
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import javax.inject.Inject

internal class ProductListServiceImpl @Inject constructor(
    @NewClient apolloClient: ApolloClient
) : GraphService(apolloClient), ProductListService {

    override suspend fun getProductList(
        after: String?,
        collectionHandle: String,
        filters: Optional<ProductFilterInput>,
        sort: ProductSortEnum,
        limit: Int
    ): Result<ProductListQuery.Data> =
        query(
            ProductListQuery(
                collectionHandle = collectionHandle,
                after = Optional.presentIfNotNull(after),
                filters = filters,
                sort = Optional.present(sort),
                limit = Optional.present(limit)
            )
        ).unwrap()

    override suspend fun searchProducts(
        after: String?,
        searchTerm: String,
        filters: Optional<ProductFilterInput>,
        sort: ProductSortEnum,
        limit: Int
    ): Result<SearchProductsQuery.Data> =
        query(
            SearchProductsQuery(
                searchTerm = searchTerm,
                after = Optional.presentIfNotNull(after),
                filters = filters,
                sort = Optional.present(sort),
                limit = Optional.present(limit)
            )
        ).unwrap()
}

internal fun ProductSortOption.toGraphQL(): ProductSortEnum = when (this) {
    ProductSortOption.RECOMMENDED -> ProductSortEnum.RELEVANCE
    ProductSortOption.MOST_RECENT -> ProductSortEnum.RECENTLY_UPDATED
    ProductSortOption.LOWEST_PRICE -> ProductSortEnum.PRICE_ASC
    ProductSortOption.HIGHEST_PRICE -> ProductSortEnum.PRICE_DESC
}

internal fun ProductListFilter?.toGraphQL(): Optional<ProductFilterInput> {
    val filter = this ?: return Optional.absent()
    return Optional.present(
        ProductFilterInput(
            brandNames = Optional.presentIfNotNull(filter.brandNames),
            minPrice = Optional.presentIfNotNull(filter.minPrice),
            maxPrice = Optional.presentIfNotNull(filter.maxPrice),
            productTypes = Optional.presentIfNotNull(filter.productTypes)
        )
    )
}
