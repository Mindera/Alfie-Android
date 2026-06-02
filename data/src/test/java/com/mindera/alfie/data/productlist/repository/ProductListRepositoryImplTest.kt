package com.mindera.alfie.data.productlist.repository

import com.mindera.alfie.data.datastore.UserPreferencesProto.ProductListLayoutModeProto
import com.mindera.alfie.data.datastore.user.UserPreferencesDataSource
import com.mindera.alfie.data.productlist.service.ProductListService
import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class ProductListRepositoryImplTest {

    @RelaxedMockK
    private lateinit var productListService: ProductListService

    @RelaxedMockK
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    @InjectMockKs
    private lateinit var repository: ProductListRepositoryImpl

    @Test
    fun `getProductList - WHEN result is success THEN return success with mapped data`() = runTest {
        val mockData = mockk<ProductListQuery.Data>(relaxed = true)
        val mockProductList = mockk<ProductListQuery.ProductList>(relaxed = true)
        val mockPageInfo = mockk<ProductListQuery.PageInfo>(relaxed = true)
        every { mockData.productList } returns mockProductList
        every { mockProductList.products } returns emptyList()
        every { mockProductList.pageInfo } returns mockPageInfo
        every { mockPageInfo.endCursor } returns null
        every { mockPageInfo.hasNextPage } returns false
        every { mockProductList.totalCount } returns 0
        coEvery { productListService.getProductList(any(), any(), any(), any(), any()) } returns Result.success(mockData)

        val result = repository.getProductList(
            after = null,
            collectionHandle = "women",
            filters = null,
            sort = ProductSortOption.RECOMMENDED,
            limit = 15
        )

        assertIs<RepositoryResult.Success<ProductList>>(result)
    }

    @Test
    fun `getProductList - WHEN result is failure THEN return error`() = runTest {
        coEvery { productListService.getProductList(any(), any(), any(), any(), any()) } returns Result.failure(mockk())

        val result = repository.getProductList(
            after = null,
            collectionHandle = "women",
            filters = null,
            sort = ProductSortOption.RECOMMENDED,
            limit = 15
        )

        assertIs<RepositoryResult.Error>(result)
    }

    @Test
    fun `updateProductListLayoutMode - WHEN there is no error THEN return success`() = runTest {
        val result = repository.updateProductListLayoutMode(ProductListLayoutMode.GRID)

        assertIs<RepositoryResult.Success<Unit>>(result)
    }

    @Test
    fun `updateProductListLayoutMode - WHEN an error happens THEN return error`() = runTest {
        coEvery { userPreferencesDataSource.updateProductListLayoutMode(any()) } throws IOException()

        val result = repository.updateProductListLayoutMode(ProductListLayoutMode.COLUMN)

        assertIs<RepositoryResult.Error>(result)
    }

    @Test
    fun `getProductListLayoutMode - returns the mapped layout mode`() = runTest {
        coEvery { userPreferencesDataSource.getProductListLayoutMode() } returns ProductListLayoutModeProto.GRID

        val result = repository.getProductListLayoutMode()

        assertEquals(ProductListLayoutMode.GRID, result)
    }
}
