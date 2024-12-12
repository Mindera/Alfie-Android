package au.com.alfie.ecomm.data.brand.repository

import au.com.alfie.ecomm.data.brand.brandData
import au.com.alfie.ecomm.data.brand.service.BrandService
import au.com.alfie.ecomm.network.exception.GraphNetworkException
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Brand
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class BrandRepositoryImplTest {

    @RelaxedMockK
    private lateinit var brandService: BrandService

    @InjectMockKs
    private lateinit var subject: BrandRepositoryImpl

    @Test
    fun `GIVEN getBrands WHEN result is successful THEN return list of brands`() = runTest {
        val expected = RepositoryResult.Success(
            listOf(
                Brand(
                    id = "123",
                    name = "QWER",
                    slug = "qwer"
                ),
                Brand(
                    id = "234",
                    name = "ASDF",
                    slug = "asdf"
                )
            )
        )
        coEvery { brandService.getBrands() } returns Result.success(brandData)

        val result = subject.getBrands()

        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN getBrands WHEN result is failure THEN exception is wrapped in error result`() = runTest {
        val expected = RepositoryResult.Error(
            ErrorResult(
                type = ErrorType.UNKNOWN,
                errorMessage = "Error",
                code = "0"
            )
        )
        coEvery { brandService.getBrands() } returns Result.failure(
            GraphNetworkException.UnexpectedException(message = "Error")
        )

        val result = subject.getBrands()

        assertEquals(expected, result)
    }
}
