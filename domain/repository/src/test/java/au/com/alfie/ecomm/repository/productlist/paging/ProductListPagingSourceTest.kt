package au.com.alfie.ecomm.repository.productlist.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Pagination
import io.mockk.coEvery
import io.mockk.every
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
    fun `correctly loads page with categoryId`() = runTest {
        val pagination = mockk<Pagination>()
        val products = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val productList = ProductList(
            products = products,
            pagination = pagination,
            title = "Women"
        )

        every { pagination.nextPage } returns null
        every { pagination.previousPage } returns null
        every { pagination.total } returns 15
        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val pager = buildPager(
            categoryId = "123564",
            query = null,
            metadataProvider = { }
        )

        val result = pager.refresh()

        assertIs<PagingSource.LoadResult.Page<Int, ProductListEntry>>(result)
        assertEquals(products, result.data)
    }

    @Test
    fun `correctly loads page with query`() = runTest {
        val pagination = mockk<Pagination>()
        val products = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val productList = ProductList(
            products = products,
            pagination = pagination,
            title = "Women"
        )

        every { pagination.nextPage } returns null
        every { pagination.previousPage } returns null
        every { pagination.total } returns 15
        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val pager = buildPager(
            categoryId = null,
            query = "something",
            metadataProvider = { }
        )

        val result = pager.refresh()

        assertIs<PagingSource.LoadResult.Page<Int, ProductListEntry>>(result)
        assertEquals(products, result.data)
    }

    @Test
    fun `correctly loads consecutive pages`() = runTest {
        val pagination = mockk<Pagination>()

        // Test first page load

        val firstProducts = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val firstProductList = ProductList(
            products = firstProducts,
            pagination = pagination,
            title = "Women"
        )

        every { pagination.nextPage } returns 15
        every { pagination.previousPage } returns null
        every { pagination.total } returns 20
        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(firstProductList)

        val pager = buildPager(
            categoryId = "123564",
            query = null,
            metadataProvider = { }
        )

        val initialLoad = pager.refresh()

        assertIs<PagingSource.LoadResult.Page<Int, ProductListEntry>>(initialLoad)
        assertEquals(firstProducts, initialLoad.data)

        // Test second page load

        val secondProducts = List(size = 5) { mockk<ProductListEntry>(relaxed = true) }
        val secondProductList = ProductList(
            products = secondProducts,
            pagination = pagination,
            title = "Women"
        )

        every { pagination.nextPage } returns null
        every { pagination.previousPage } returns 0
        every { pagination.total } returns 20
        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(secondProductList)

        val secondLoad = pager.append()

        assertIs<PagingSource.LoadResult.Page<Int, ProductListEntry>>(secondLoad)
        assertEquals(secondProducts, secondLoad.data)
    }

    @Test
    fun `fails when request fails`() = runTest {
        val errorResult = mockk<ErrorResult>()

        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val pager = buildPager(
            categoryId = "123564",
            query = null,
            metadataProvider = { }
        )

        val result = pager.refresh()

        assertIs<PagingSource.LoadResult.Error<Int, ProductListEntry>>(result)
        assertEquals(errorResult, result.throwable)
        assertNull(pager.getLastLoadedPage())
    }

    @Test
    fun `metadataProvider is called when request succeeds`() = runTest {
        val pagination = mockk<Pagination>()
        val products = List(size = 15) { mockk<ProductListEntry>(relaxed = true) }
        val productList = ProductList(
            products = products,
            pagination = pagination,
            title = "Women"
        )

        val metadataProvider: (ProductListMetadata) -> Unit = mockk(relaxed = true)
        val expectedMetadata = ProductListMetadata(
            title = "Women",
            totalResults = 15,
        )

        every { pagination.nextPage } returns null
        every { pagination.previousPage } returns null
        every { pagination.total } returns 15
        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val pager = buildPager(
            categoryId = "123564",
            query = null,
            metadataProvider = metadataProvider
        )

        pager.refresh()

        verify { metadataProvider(expectedMetadata) }
    }

    @Test
    fun `metadataProvider is not called when request fails`() = runTest {
        val errorResult = mockk<ErrorResult>()

        coEvery {
            repository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val metadataProvider: (ProductListMetadata) -> Unit = mockk(relaxed = true)

        val pager = buildPager(
            categoryId = "123564",
            query = null,
            metadataProvider = metadataProvider
        )

        pager.refresh()

        verify(exactly = 0) { metadataProvider(any()) }
    }

    private fun buildPager(
        categoryId: String?,
        query: String?,
        metadataProvider: (ProductListMetadata) -> Unit
    ) = TestPager(
        config = config,
        pagingSource = ProductListPagingSource(
            productListRepository = repository,
            categoryId = categoryId,
            query = query,
            metadataProvider = metadataProvider
        )
    )
}
