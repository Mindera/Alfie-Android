package au.com.alfie.ecomm.domain.usecase.brand

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.brand.BrandRepository
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Brand
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
internal class GetBrandsUseCaseTest {

    @RelaxedMockK
    private lateinit var brandRepository: BrandRepository

    @InjectMockKs
    lateinit var subject: GetBrandsUseCase

    @Test
    fun `invoke - returns list of brands`() = runTest {
        val mockBrands = mockk<List<Brand>>()
        coEvery { brandRepository.getBrands() } returns RepositoryResult.Success(mockBrands)

        val expected = UseCaseResult.Success(mockBrands)

        val result = subject()

        assertEquals(expected, result)
    }
}
