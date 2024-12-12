package au.com.alfie.ecomm.domain.usecase.productlist

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
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
            productListRepository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val result = useCase(
            offset = 0,
            limit = 15,
            categoryId = "12323164",
            query = null
        )

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        val expected = UseCaseResult.Error(errorResult)

        coEvery {
            productListRepository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Error(errorResult)

        val result = useCase(
            offset = 0,
            limit = 15,
            categoryId = null,
            query = null
        )

        assertEquals(expected, result)
    }
}
