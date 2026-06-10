package com.mindera.alfie.repository.productlist.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class)
class ProductListPagingSourceTest {

    companion object {
        private val config = PagingConfig(pageSize = 15)
    }

    @MockK
    private lateinit var repository: ProductListRepository

    @Test
    fun `correctly loads first page`() = runTest {
        val products = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val productList = ProductList(
            products = products,
            pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 15)
        )
        coEvery {
            repository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val pager = buildPager(metadataProvider = { })
        val result = pager.refresh()

        assertIs<PagingSource.LoadResult.Page<String, ProductListEntry>>(result)
        assertEquals(products, result.data)
        assertNull(result.nextKey)
    }

    @Test
    fun `correctly loads consecutive pages with cursor`() = runTest {
        val firstProducts = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        coEvery {
            repository.getProductList(null, any(), any(), any(), any())
        } returns RepositoryResult.Success(
            ProductList(
                products = firstProducts,
                pagination = CursorPagination(endCursor = "cursor123", hasNextPage = true, totalCount = 20)
            )
        )

        val secondProducts = List(size = 5) { mockk<ProductListEntry>(relaxed = true) }
        coEvery {
            repository.getProductList("cursor123", any(), any(), any(), any())
        } returns RepositoryResult.Success(
            ProductList(
                products = secondProducts,
                pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 20)
            )
        )

        val pager = buildPager(metadataProvider = { })
        val initialLoad = pager.refresh()

        assertIs<PagingSource.LoadResult.Page<String, ProductListEntry>>(initialLoad)
        assertEquals(firstProducts, initialLoad.data)
        assertEquals("cursor123", initialLoad.nextKey)

        val secondLoad = pager.append()

        assertIs<PagingSource.LoadResult.Page<String, ProductListEntry>>(secondLoad)
        assertEquals(secondProducts, secondLoad.data)
        assertNull(secondLoad.nextKey)
    }

    @Test
    fun `fails when request fails`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery {
            repository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val pager = buildPager(metadataProvider = { })
        val result = pager.refresh()

        assertIs<PagingSource.LoadResult.Error<String, ProductListEntry>>(result)
        assertEquals(errorResult, result.throwable)
        assertNull(pager.getLastLoadedPage())
    }

    @Test
    fun `metadataProvider is called when request succeeds`() = runTest {
        val products = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val productList = ProductList(
            products = products,
            pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 15)
        )
        val metadataProvider: (ProductListMetadata) -> Unit = mockk(relaxed = true)
        val expectedMetadata = ProductListMetadata(totalResults = 15)

        coEvery {
            repository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val pager = buildPager(metadataProvider = metadataProvider)
        pager.refresh()

        verify { metadataProvider(expectedMetadata) }
    }

    @Test
    fun `metadataProvider is not called when request fails`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery {
            repository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val metadataProvider: (ProductListMetadata) -> Unit = mockk(relaxed = true)
        val pager = buildPager(metadataProvider = metadataProvider)
        pager.refresh()

        verify(exactly = 0) { metadataProvider(any()) }
    }

    private fun buildPager(
        source: ProductListQuerySource = ProductListQuerySource.Collection("women"),
        filters: ProductListFilter? = null,
        sort: ProductSortOption = ProductSortOption.RECOMMENDED,
        metadataProvider: (ProductListMetadata) -> Unit
    ) = TestPager(
        config = config,
        pagingSource = ProductListPagingSource(
            productListRepository = repository,
            source = source,
            filters = filters,
            sort = sort,
            metadataProvider = metadataProvider
        )
    )
}
