package com.mindera.alfie.integrationtest

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.graphql.bff.SearchProductsQuery
import com.mindera.alfie.graphql.bff.type.ProductSortEnum
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** `searchProducts` against the local BFF: happy path and cursor pagination. */
internal class SearchProductsIntegrationTest : BffIntegrationTest() {

    @Test
    fun searchProducts_happyPath_returnsResults() = withBff { client ->
        val term = deriveSearchTerm(client) ?: return@withBff

        val response = client.executeOrFail(searchQuery(term, limit = 5)).searchProducts.productListResponseFragment

        assertTrue(response.products.isNotEmpty(), "Expected search results for '$term'")
    }

    @Test
    fun searchProducts_cursorPagination_returnsDistinctNextPage() = withBff { client ->
        val term = deriveSearchTerm(client) ?: return@withBff

        val firstPage = client.executeOrFail(searchQuery(term, limit = 1)).searchProducts.productListResponseFragment
        if (firstPage.pageInfo?.hasNextPage != true) return@withBff

        val cursor = requireNotNull(firstPage.pageInfo?.endCursor) { "hasNextPage but no endCursor" }
        val secondPage = client.executeOrFail(searchQuery(term, limit = 1, after = cursor))
            .searchProducts.productListResponseFragment

        assertTrue(secondPage.products.isNotEmpty(), "Second page should have results")
        val firstIds = firstPage.products.map { it.productListEntryFragment.id }.toSet()
        assertTrue(
            secondPage.products.none { it.productListEntryFragment.id in firstIds },
            "Page 2 should not repeat page 1 results"
        )
    }

    /** Derives a query likely to match by reusing the first word of a real product name. */
    private suspend fun deriveSearchTerm(client: ApolloClient): String? {
        val list = client.executeOrFail(
            ProductListQuery(collectionHandle = DEFAULT_COLLECTION_HANDLE, limit = Optional.present(1))
        ).productList.productListResponseFragment
        val name = list.products.firstOrNull()?.productListEntryFragment?.name ?: return null
        return name.trim().substringBefore(' ').takeIf { it.isNotBlank() }
    }

    private fun searchQuery(term: String, limit: Int, after: String? = null) = SearchProductsQuery(
        searchTerm = term,
        after = Optional.presentIfNotNull(after),
        sort = Optional.present(ProductSortEnum.RELEVANCE),
        limit = Optional.present(limit),
        // searchProducts requires an explicit platform (unlike productList, which defaults it server-side).
        platform = Optional.present(PLATFORM)
    )
}
