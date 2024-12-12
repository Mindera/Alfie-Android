package au.com.alfie.ecomm.domain.usecase.productlist

import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class GetProductListLayoutModeUseCaseTest {

    @RelaxedMockK
    lateinit var productListRepository: ProductListRepository

    @InjectMockKs
    lateinit var useCase: GetProductListLayoutModeUseCase

    @Test
    fun `invoke - WHEN repository returns GRID THEN return GRID`() = runTest {
        coEvery { productListRepository.getProductListLayoutMode() } returns ProductListLayoutMode.GRID

        val result = useCase()

        assertEquals(ProductListLayoutMode.GRID, result)
    }

    @Test
    fun `invoke - WHEN repository returns COLUMN THEN return COLUMN`() = runTest {
        coEvery { productListRepository.getProductListLayoutMode() } returns ProductListLayoutMode.COLUMN

        val result = useCase()

        assertEquals(ProductListLayoutMode.COLUMN, result)
    }
}
