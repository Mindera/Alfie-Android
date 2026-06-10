package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.ErrorResult
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
class GetProductListUseCaseTest {

    @RelaxedMockK
    lateinit var productListRepository: ProductListRepository

    @InjectMockKs
    lateinit var useCase: GetProductListUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val productList = mockk<ProductList>()
        val expected = UseCaseResult.Success(productList)

        coEvery {
            productListRepository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val result = useCase(
            after = null,
            source = ProductListQuerySource.Collection("women"),
            filters = null,
            sort = ProductSortOption.RECOMMENDED,
            limit = 15
        )

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        val expected = UseCaseResult.Error(errorResult)

        coEvery {
            productListRepository.getProductList(any(), any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val result = useCase(
            after = null,
            source = ProductListQuerySource.Collection("women"),
            filters = ProductListFilter(brandNames = listOf("Brand"), minPrice = null, maxPrice = null, productTypes = null),
            sort = ProductSortOption.LOWEST_PRICE,
            limit = 15
        )

        assertEquals(expected, result)
    }
}
