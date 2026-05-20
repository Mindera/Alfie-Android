package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
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
class UpdateProductListLayoutModeUseCaseTest {

    @RelaxedMockK
    lateinit var productListRepository: ProductListRepository

    @InjectMockKs
    lateinit var useCase: UpdateProductListLayoutModeUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val expected = UseCaseResult.Success(Unit)

        coEvery { productListRepository.updateProductListLayoutMode(any()) } returns RepositoryResult.Success(Unit)

        val result = useCase(ProductListLayoutMode.GRID)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        val expected = UseCaseResult.Error(errorResult)

        coEvery { productListRepository.updateProductListLayoutMode(any()) } returns RepositoryResult.Error(errorResult)

        val result = useCase(ProductListLayoutMode.COLUMN)

        assertEquals(expected, result)
    }
}
