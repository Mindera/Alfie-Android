package com.mindera.alfie.integrationtest

import com.apollographql.apollo.api.Optional
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.graphql.bff.SearchProductsQuery
import com.mindera.alfie.graphql.bff.type.ProductSortEnum
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * `searchProducts` against the Mock BFF: happy path and cursor pagination.
 *
 * Pinned to [SeedAnchors.SearchTerms.many], which matches more than 10 products.
 */
internal class SearchProductsIntegrationTest : BffIntegrationTest() {

    @Test
    fun searchProducts_happyPath_returnsResults() = withBff { client ->
        val response = client.executeOrFail(searchQuery(TERM, limit = 5)).searchProducts.productListResponseFragment

        assertTrue(response.products.isNotEmpty(), "Expected search results for '$TERM'")
    }

    @Test
    fun searchProducts_cursorPagination_returnsDistinctNextPage() = withBff { client ->
        val firstPage = client.executeOrFail(searchQuery(TERM, limit = 1)).searchProducts.productListResponseFragment
        assertTrue(firstPage.pageInfo?.hasNextPage == true, "'$TERM' should match more than one page")

        val cursor = requireNotNull(firstPage.pageInfo?.endCursor) { "hasNextPage but no endCursor" }
        val secondPage = client.executeOrFail(searchQuery(TERM, limit = 1, after = cursor))
            .searchProducts.productListResponseFragment

        assertTrue(secondPage.products.isNotEmpty(), "Second page should have results")
        val firstIds = firstPage.products.map { it.productListEntryFragment.id }.toSet()
        assertTrue(
            secondPage.products.none { it.productListEntryFragment.id in firstIds },
            "Page 2 should not repeat page 1 results"
        )
    }

    private fun searchQuery(term: String, limit: Int, after: String? = null) = SearchProductsQuery(
        searchTerm = term,
        after = Optional.presentIfNotNull(after),
        sort = Optional.present(ProductSortEnum.RELEVANCE),
        limit = Optional.present(limit)
    )

    private companion object {
        const val TERM = SeedAnchors.SearchTerms.many
    }
}
