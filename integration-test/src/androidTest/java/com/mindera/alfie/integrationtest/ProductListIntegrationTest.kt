package com.mindera.alfie.integrationtest

import com.apollographql.apollo.api.Optional
import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.graphql.bff.type.ProductFilterInput
import com.mindera.alfie.graphql.bff.type.ProductSortEnum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * `productList` against the Mock BFF: happy path, cursor pagination, sort, filter.
 *
 * Pinned to [SeedAnchors.Collections.women], which holds more than one page at the default limit.
 */
internal class ProductListIntegrationTest : BffIntegrationTest() {

    @Test
    fun productList_happyPath_returnsProducts() = withBff { client ->
        val response = client.executeOrFail(productListQuery(limit = 5)).productList.productListResponseFragment

        assertTrue(response.products.isNotEmpty(), "Expected products for '$COLLECTION'")
        response.products.forEach { product ->
            val entry = product.productListEntryFragment
            assertTrue(entry.id.isNotBlank(), "Product id should not be blank")
            assertTrue(entry.slug.isNotBlank(), "Product slug should not be blank")
            assertTrue(entry.name.isNotBlank(), "Product name should not be blank")
        }
    }

    @Test
    fun productList_cursorPagination_returnsDistinctNextPage() = withBff { client ->
        val firstPage = client.executeOrFail(productListQuery(limit = 2)).productList.productListResponseFragment
        assertTrue(firstPage.pageInfo?.hasNextPage == true, "'$COLLECTION' should have more than one page")

        val cursor = requireNotNull(firstPage.pageInfo?.endCursor) { "hasNextPage but no endCursor" }
        val secondPage = client.executeOrFail(productListQuery(limit = 2, after = cursor))
            .productList.productListResponseFragment

        assertTrue(secondPage.products.isNotEmpty(), "Second page should have products")
        val firstIds = firstPage.products.map { it.productListEntryFragment.id }.toSet()
        assertTrue(
            secondPage.products.none { it.productListEntryFragment.id in firstIds },
            "Page 2 should not repeat page 1 products"
        )
    }

    @Test
    fun productList_sortByPriceAsc_returnsNonDecreasingPrices() = withBff { client ->
        val response = client.executeOrFail(productListQuery(limit = 10, sort = ProductSortEnum.PRICE_ASC))
            .productList.productListResponseFragment

        val prices = response.products.map {
            it.productListEntryFragment.priceRange.minVariantPrice.moneyFragment.amount.toBigDecimal()
        }
        assertTrue(prices.size > 1, "Expected several products to compare prices")
        assertEquals(prices.sorted(), prices, "PRICE_ASC should return non-decreasing min prices")
    }

    @Test
    fun productList_filterByBrandKey_returnsOnlyThatBrand() = withBff { client ->
        val brandKey = SeedAnchors.BrandKeys.alfie

        val filtered = client.executeOrFail(
            productListQuery(limit = 10, filters = ProductFilterInput(brandNames = Optional.present(listOf(brandKey))))
        ).productList.productListResponseFragment

        val brandNames = filtered.products.map { it.productListEntryFragment.brandName }
        assertTrue(brandNames.isNotEmpty(), "Expected results for brand key '$brandKey'")
        assertTrue(brandNames.all { it != null }, "Every result should have a brand: $brandNames")
        assertEquals(1, brandNames.toSet().size, "Every result should be the same brand: $brandNames")
    }

    private fun productListQuery(
        limit: Int,
        after: String? = null,
        sort: ProductSortEnum = ProductSortEnum.NEWEST,
        filters: ProductFilterInput? = null
    ) = ProductListQuery(
        collectionHandle = COLLECTION,
        after = Optional.presentIfNotNull(after),
        filters = Optional.presentIfNotNull(filters),
        sort = Optional.present(sort),
        limit = Optional.present(limit)
    )

    private companion object {
        const val COLLECTION = SeedAnchors.Collections.women
    }
}
