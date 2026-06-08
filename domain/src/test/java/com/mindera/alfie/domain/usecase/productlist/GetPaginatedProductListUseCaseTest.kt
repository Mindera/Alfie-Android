package com.mindera.alfie.domain.usecase.productlist

import androidx.paging.testing.asSnapshot
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class GetPaginatedProductListUseCaseTest {

    @RelaxedMockK
    lateinit var productListRepository: ProductListRepository

    @InjectMockKs
    lateinit var useCase: GetPaginatedProductListUseCase

    @Test
    fun `invoke - returns a paging flow`() = runTest {
        val product = mockk<ProductListEntry>(relaxed = true)
        val products = List(size = 15) { product }
        val pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 15)
        val productList = ProductList(
            pagination = pagination,
            products = products
        )
        coEvery {
            productListRepository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val result = useCase(
            collectionHandle = "women",
            filters = null,
            sort = ProductSortOption.RECOMMENDED,
            metadataProvider = { assertEquals(15, it.totalResults) }
        ).asSnapshot()

        assertEquals(products, result)
    }
}
