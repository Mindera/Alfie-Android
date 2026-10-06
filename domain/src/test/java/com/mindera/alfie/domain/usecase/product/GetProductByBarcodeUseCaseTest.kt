package com.mindera.alfie.domain.usecase.product

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.BarcodeMatch
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
internal class GetProductByBarcodeUseCaseTest {

    @RelaxedMockK
    lateinit var productRepository: ProductRepository

    @InjectMockKs
    lateinit var subject: GetProductByBarcodeUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val mockMatch = mockk<BarcodeMatch>()

        coEvery {
            productRepository.getProductByBarcode(barcode = BARCODE)
        } returns RepositoryResult.Success(mockMatch)

        val expected = UseCaseResult.Success(mockMatch)

        val result = subject(barcode = BARCODE)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val mockError = mockk<ErrorResult>()

        coEvery {
            productRepository.getProductByBarcode(barcode = BARCODE)
        } returns RepositoryResult.Error(mockError)

        val expected = UseCaseResult.Error(mockError)

        val result = subject(barcode = BARCODE)

        assertEquals(expected, result)
    }

    private companion object {
        const val BARCODE = "5012345678900"
    }
}
