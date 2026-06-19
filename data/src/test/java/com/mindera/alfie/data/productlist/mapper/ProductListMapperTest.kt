package com.mindera.alfie.data.productlist.mapper

import com.mindera.alfie.graphql.bff.fragment.ImageFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.graphql.bff.fragment.ProductListEntryFragment
import com.mindera.alfie.graphql.bff.fragment.ProductListResponseFragment
import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import com.mindera.alfie.repository.shared.model.Media
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProductListMapperTest {

    private fun buildMoneyFragment(amount: Double, currency: String) = mockk<MoneyFragment>().also {
        every { it.amount } returns amount
        every { it.currencyCode } returns currency
    }

    private fun buildPriceRange(min: Double, max: Double, currency: String): ProductListEntryFragment.PriceRange {
        val minMoney = buildMoneyFragment(min, currency)
        val maxMoney = buildMoneyFragment(max, currency)
        val minPrice = mockk<ProductListEntryFragment.MinVariantPrice>().also {
            every { it.moneyFragment } returns minMoney
        }
        val maxPrice = mockk<ProductListEntryFragment.MaxVariantPrice>().also {
            every { it.moneyFragment } returns maxMoney
        }
        return mockk<ProductListEntryFragment.PriceRange>().also {
            every { it.minVariantPrice } returns minPrice
            every { it.maxVariantPrice } returns maxPrice
        }
    }

    private fun buildFragment(
        id: String = "123",
        slug: String = "123-product",
        name: String = "Product",
        brandName: String? = "Brand",
        productType: String? = "Clothing",
        primaryImage: ProductListEntryFragment.PrimaryImage? = null,
        priceRange: ProductListEntryFragment.PriceRange = buildPriceRange(100.0, 200.0, "AUD"),
        inventoryTotal: Int? = 5,
        tags: List<String?>? = listOf("Best Seller")
    ): ProductListEntryFragment = mockk<ProductListEntryFragment>().also {
        every { it.id } returns id
        every { it.slug } returns slug
        every { it.name } returns name
        every { it.brandName } returns brandName
        every { it.productType } returns productType
        every { it.primaryImage } returns primaryImage
        every { it.priceRange } returns priceRange
        every { it.inventoryTotal } returns inventoryTotal
        every { it.tags } returns tags
    }

    private fun buildProductList(
        fragments: List<ProductListEntryFragment>,
        endCursor: String? = "cursor123",
        hasNextPage: Boolean = false,
        totalCount: Int = fragments.size
    ): ProductListResponseFragment {
        val products = fragments.map { frag ->
            mockk<ProductListResponseFragment.Product>().also { p ->
                every { p.productListEntryFragment } returns frag
            }
        }
        val pageInfo = mockk<ProductListResponseFragment.PageInfo>().also {
            every { it.endCursor } returns endCursor
            every { it.hasNextPage } returns hasNextPage
        }
        return mockk<ProductListResponseFragment>().also {
            every { it.products } returns products
            every { it.pageInfo } returns pageInfo
            every { it.totalCount } returns totalCount
        }
    }

    @Test
    fun `toDomain - maps products and pagination correctly`() = runTest {
        val productList = buildProductList(
            fragments = listOf(buildFragment()),
            endCursor = "cursor",
            hasNextPage = true,
            totalCount = 1
        )

        val result = productList.toDomain()

        assertEquals(1, result.products.size)
        val entry = result.products.first()
        assertEquals("123", entry.id)
        assertEquals("Brand", entry.brandName)
        assertEquals("Clothing", entry.productType)
        assertNull(entry.primaryImage)
        assertEquals(ProductListPriceRange(100.0, 200.0, "AUD"), entry.priceRange)
        assertEquals(listOf("Best Seller"), entry.tags)
        assertEquals(CursorPagination(endCursor = "cursor", hasNextPage = true, totalCount = 1), result.pagination)
    }

    @Test
    fun `toDomain - maps primaryImage when present`() = runTest {
        val imageFragment = mockk<ImageFragment>().also {
            every { it.url } returns "https://example.com/img.jpg"
            every { it.altText } returns "Alt text"
        }
        val primaryImage = mockk<ProductListEntryFragment.PrimaryImage>().also {
            every { it.imageFragment } returns imageFragment
        }
        val productList = buildProductList(
            fragments = listOf(buildFragment(primaryImage = primaryImage, tags = null))
        )

        val result = productList.toDomain()

        val entry = result.products.first()
        assertEquals(Media.Image(url = "https://example.com/img.jpg", alt = "Alt text"), entry.primaryImage)
        assertTrue(entry.tags.isEmpty())
    }

    @Test
    fun `toDomain - maps null tags to empty list`() = runTest {
        val productList = buildProductList(
            fragments = listOf(buildFragment(tags = null))
        )

        val result = productList.toDomain()

        assertTrue(result.products.first().tags.isEmpty())
    }

    @Test
    fun `toDomain - maps pagination with no next page`() = runTest {
        val productList = buildProductList(
            fragments = listOf(buildFragment()),
            endCursor = null,
            hasNextPage = false,
            totalCount = 1
        )

        val result = productList.toDomain()

        assertEquals(CursorPagination(endCursor = null, hasNextPage = false, totalCount = 1), result.pagination)
    }
}
